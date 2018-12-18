package com.zq.dfs.data;

import com.zq.dfs.*;
import com.zq.dfs.constants.IFileSystemConstants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: ZQDFS
 * @description: 内存文件系统
 * @author: zhouqi1s
 * @create: 2018-12-11 11:18
 **/
public class IDataFileSystem implements IFileSystem {

    /**
     * 索引及目录映射
     */
    protected IndexTables indexTables;

    /**
     * 数据块池
     */
    protected BlockPool blockPool;

    public IDataFileSystem() {
        this.blockPool = new DataBlockPool();
        this.indexTables = new DataIndexTables(blockPool);
    }

    @Override
    public IDirectory root() {
        return (IDirectory) indexTables.root();
    }

    @Override
    public IFileInputStream open(String filePath) throws IOException{
        if(!exists(filePath)){
            throw new FileNotFoundException(filePath);
        }
        INode node = indexTables.find(filePath);
        if(node.isDirectory()){
            throw new IOException(String.format("not open a directory:", filePath));
        }
        IFile file = (IFile) node;
        return new IFileInputStream(file);
    }

    @Override
    public IFileOutputStream append(String filePath) throws IOException {
        if(!exists(filePath)){
            throw new FileNotFoundException(filePath);
        }
        INode node = indexTables.find(filePath);
        if(node.isDirectory()){
            throw new IOException(String.format("not open a directory:%s", filePath));
        }
        IFile file = (IFile) node;
        return new IFileOutputStream(file);
    }

    @Override
    public INode find(String path) {
        return indexTables.find(path);
    }

    @Override
    public IFile createFile(String path) throws IOException {
        if(!isFilePath(path)){
            throw new IOException(String.format("this is not a file path:%s", path));
        }

        String fileParentPath = path.substring(0, path.lastIndexOf(IFileSystemConstants.PATH_SEPARATOR)+1);
        if(!exists(fileParentPath)){
            mkdirs(fileParentPath);
        }
        INode fileParent = find(fileParentPath);
        IFile file = new IDataBlockFile(blockPool, fileParent, path);
        indexTables.put(path, file);
        return file;
    }

    private boolean isFilePath(String path){
        return path.indexOf(IFileSystemConstants.PATH_SEPARATOR) == 0
                && path.lastIndexOf(IFileSystemConstants.PATH_SEPARATOR) != path.length() -1;
    }

    @Override
    public IDirectory mkdirs(String path) throws IOException {
        if(!isDirectoryPath(path)){
            throw new IOException(String.format("this is not a file path:%s", path));
        }
        if(exists(path)){
            return (IDirectory) find(path);
        }
        IDirectory directory = (IDirectory) indexTables.root();
        String[] params = path.split(IFileSystemConstants.PATH_SEPARATOR);
        String currentPath = indexTables.root().path();
        for(String param : params){
            if(param.equals("")){
                continue;
            }
            currentPath += param + IFileSystemConstants.PATH_SEPARATOR;
            if(!exists(currentPath)){
                IDirectory currentDirectory = new IDataDirectory(directory, currentPath);
                directory.addDirectory(currentDirectory);
                indexTables.put(currentPath, currentDirectory);
            }
            directory = (IDirectory) find(currentPath);
        }
        return directory;
    }

    private boolean isDirectoryPath(String path) {
        return path.indexOf(IFileSystemConstants.PATH_SEPARATOR) == 0
                && path.lastIndexOf(IFileSystemConstants.PATH_SEPARATOR) == path.length() -1;
    }

    @Override
    public void delete(String path) {
        if(exists(path)){
            INode node = find(path);
            node.distory();
            IDirectory parent = (IDirectory) node.parent();
            if(node.isDirectory()){
                parent.remove((IDirectory)node);
            }else {
                parent.remove((IFile)node);
            }
            node.parent(null);
            indexTables.remove(path);
        }
    }

    @Override
    public void rename(String srcPath, String targetPath) throws IOException {
        if(!exists(srcPath)){
            throw new IOException(String.format("srcPath:%s is not exists", srcPath));
        }
        INode srcNode = find(srcPath);
        indexTables.remove(srcPath);

        String targetParentPath = targetPath.substring(0, targetPath.lastIndexOf(IFileSystemConstants.PATH_SEPARATOR)+1);
        String targetName  = targetPath.substring(targetPath.lastIndexOf(IFileSystemConstants.PATH_SEPARATOR)+1);
        if(!exists(targetParentPath)){
            mkdirs(targetParentPath);
        }
        IDirectory targetParentNode = (IDirectory) find(targetParentPath);
        srcNode.parent(targetParentNode);
        srcNode.name(targetName);
        targetParentNode.addFile((IFile)srcNode);
        indexTables.put(targetPath, srcNode);
    }

    @Override
    public void close() {
        indexTables.close();
        blockPool.close();
    }

    @Override
    public boolean exists(String path){
        return indexTables.exists(path);
    }
}
