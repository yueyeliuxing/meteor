package com.zq.dfs.data;

import com.zq.dfs.*;
import com.zq.dfs.constants.IFileSystemConstants;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: ZQDFS
 * @description: 内存 索引目录
 * @author: zhouqi1
 * @create: 2018-12-11 14:29
 **/
public class DataIndexTables implements IndexTables, Persistencer {

    /**
     * 根目录
     */
    private INode root;

    /**
     * 索引及目录映射
     */
    private Map<String, INode> inodesOfpath;

    /**
     * 块池
     */
    private BlockPool blockPool;

    public DataIndexTables(BlockPool blockPool) {
        root = new IDataDirectory(null, IFileSystemConstants.ROOT_DIRECTORY_PATH);
        inodesOfpath = new HashMap<>();
        inodesOfpath.put(root.path(), root);
        this.blockPool = blockPool;
    }

    @Override
    public INode root() {
        return root;
    }

    public BlockPool blockPool() {
        return blockPool;
    }

    @Override
    public boolean exists(String path) {
        return inodesOfpath.containsKey(path);
    }

    @Override
    public INode find(String path) {
        return inodesOfpath.get(path);
    }

    @Override
    public void put(String path, INode node) {
        inodesOfpath.put(path, node);
    }

    @Override
    public void remove(String path) {
        INode node = find(path);
        if(node == null){
            return;
        }
        if(node.isDirectory()){
            for(String key : inodesOfpath.keySet()){
                if(key.startsWith(path)){
                    inodesOfpath.remove(key);
                }
            }
        }else {
            inodesOfpath.remove(path);
        }
    }

    @Override
    public void close() {
        root = null;
        for(INode node : inodesOfpath.values()){
            node.close();
        }
        inodesOfpath.clear();
    }

    @Override
    public byte[] serialize() {
        int nodesSize = 0;
        int nodesValueLen = 0;
        List<byte[]> nodeBytes = new ArrayList<>();
        List<Byte> nodeTypeBytes = new ArrayList<>();
        if(inodesOfpath != null){
            nodesSize = inodesOfpath.size();
            for(INode node : inodesOfpath.values()){
                byte[] nodeByte = node.serialize();
                nodesValueLen += 1;
                nodesValueLen += 4;
                nodesValueLen += nodeByte.length;
                nodeBytes.add(nodeByte);
                nodeTypeBytes.add(node.isDirectory() ? (byte)0 : (byte)1);
            }
        }
        int capacity = 4 + nodesValueLen;
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
        byteBuffer.putInt(nodesSize);
        if(nodesSize > 0){
            int index = 0;
            for(byte[] nodeByte : nodeBytes){
                byteBuffer.put(nodeTypeBytes.get(index++));
                byteBuffer.putInt(nodeByte.length);
                byteBuffer.put(nodeByte);
            }
        }
        return byteBuffer.array();
    }

    @Override
    public void deserialize(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        int nodesSize = byteBuffer.getInt();
        if(nodesSize > 0) {
            for (int i = 0; i < nodesSize; i++) {
                byte type = byteBuffer.get();
                int nodeByteLen = byteBuffer.getInt();
                byte[] nodeByte = new byte[nodeByteLen];
                byteBuffer.get(nodeByte);
                INode node = type == 0 ? new IDataDirectory() : new IDataBlockFile(blockPool);
                node.deserialize(nodeByte);
                inodesOfpath.put(node.path(), node);
            }

            /**
             * 组装树结构
             */
            assembleDirectory((IDirectory) inodesOfpath.get(IFileSystemConstants.ROOT_DIRECTORY_PATH));

        }
    }

    /**
     * 组装node 为树结构
     * @param directory
     */
    private void assembleDirectory(IDirectory directory) {
        List<IDirectory> subdirectories =  directory.subdirectories();
        List<IFile> subfiles =  directory.subFiles();
        if(subdirectories != null && !subdirectories.isEmpty()){
            for(IDirectory subdirectory : subdirectories){
                directory.remove(subdirectory);
                String subdirectoryPath = subdirectory.path();
                IDirectory directory1 = (IDirectory) inodesOfpath.get(subdirectoryPath);
                if(directory1 != null){
                    directory1.parent(directory);
                    directory.addDirectory(directory1);
                    assembleDirectory(directory1);
                }
            }
        }
        if(subfiles != null && !subfiles.isEmpty()){
            for(IFile subfile : subfiles){
                directory.remove(subfile);
                String subfilePath = subfile.path();
                IFile file = (IFile) inodesOfpath.get(subfilePath);
                if(file != null){
                    file.parent(directory);
                    directory.addFile(file);
                }
            }
        }
    }
}
