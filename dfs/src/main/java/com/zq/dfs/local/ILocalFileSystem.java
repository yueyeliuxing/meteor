package com.zq.dfs.local;

import com.zq.dfs.*;
import com.zq.dfs.filesystem.*;
import com.zq.dfs.config.Configuration;
import com.zq.dfs.config.FileSystemConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @program: ZQDFS
 * @description: 本地文件系统
 * @author: zhouqi1
 * @create: 2018-12-11 19:21
 **/
public class ILocalFileSystem implements IFileSystem {

    private IndexTables indexTables;

    public ILocalFileSystem() {
        this.indexTables = new LocalIndexTables();
    }

    @Override
    public IFileInputStream open(String filePath) throws IOException {
        if(!exists(filePath)){
            throw new FileNotFoundException(filePath);
        }
        INode node = indexTables.find(filePath);
        if(node.isDirectory()){
            throw new IOException(String.format("not open a directory:", filePath));
        }
        return new IFileInputStream((ILocalFile)node);
    }

    @Override
    public IFileOutputStream append(String filePath) throws IOException {
        if(!exists(filePath)){
            throw new FileNotFoundException(filePath);
        }
        INode node = indexTables.find(filePath);
        if(node.isDirectory()){
            throw new IOException(String.format("not open a directory:", filePath));
        }
        return new IFileOutputStream((ILocalFile)node);
    }

    @Override
    public IFile createFile(String path) throws IOException {
        File file = new File(path);
        if(!file.isFile()){
            throw new IOException(String.format("this is not a file path:%s", path));
        }
        file.createNewFile();
        return new ILocalFile(path);
    }

    @Override
    public IDirectory mkdirs(String path) throws IOException {
        File file = new File(path);
        if(!file.isDirectory()){
            throw new IOException(String.format("this is not a directory path:%s", path));
        }
        file.mkdirs();
        return new ILocalDirectory(path);
    }

    @Override
    public boolean exists(String path) {
        return indexTables.exists(path);
    }

    @Override
    public void delete(String path) {
        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
    }

    @Override
    public void rename(String srcPath, String targetPath) throws IOException {
        File file = new File(srcPath);
        if(!file.exists()){
            throw new IOException(String.format("srcPath:%s is not exists", srcPath));
        }
        file.renameTo(new File(targetPath));
    }

    @Override
    public void close() {
        indexTables.clear();
    }


}
