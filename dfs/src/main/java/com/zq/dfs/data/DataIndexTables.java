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

    public DataIndexTables() {
        root = new IDataDirectory(null, IFileSystemConstants.ROOT_DIRECTORY_PATH);
        inodesOfpath = new HashMap<>();
        inodesOfpath.put(root.path(), root);
    }

    @Override
    public INode root() {
        return root;
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
        IDirectory parent = (IDirectory) node.parent();
        if(node.isDirectory()){
            parent.remove((IDirectory)node);
            for(String key : inodesOfpath.keySet()){
                if(key.startsWith(path)){
                    inodesOfpath.remove(key);
                }
            }
        }else {
            parent.remove((IFile)node);
            inodesOfpath.remove(path);
        }
        node.parent(null);

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
        if(nodesSize > 0){
            for(int i = 0; i < nodesSize; i++){
                byte type = byteBuffer.get();
                int nodeByteLen = byteBuffer.getInt();
                byte[] nodeByte = new byte[nodeByteLen];
                byteBuffer.get(nodeByte);
                INode node = type == 0 ? new IDataDirectory() : new IDataBlockFile();
                node.deserialize(nodeByte);
                inodesOfpath.put(node.path(), node);
            }
        }
    }
}
