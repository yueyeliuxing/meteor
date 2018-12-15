package com.zq.dfs.data;

import com.zq.dfs.*;
import com.zq.dfs.constants.IFileSystemConstants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 可装载其他文件系统的文件系统
 */
public class ILoadabledFileSystem implements IFileSystem {

    /**
     * 目标文件系统
     */
    private IFileSystem fileSystem;

    /**
     * 其他文件系统目录
     */
    private Map<String, IFileSystemDirectory> fileSystemDirectoriesOfPath;

    public ILoadabledFileSystem(IFileSystem fileSystem) {
        this.fileSystem = fileSystem;
        fileSystemDirectoriesOfPath = new ConcurrentHashMap<>();
    }



    @Override
    public IDirectory root() {
        return fileSystem.root();
    }

    @Override
    public INode find(String path) {
        IFileSystemDirectory directory = findFileSystemDirectory(path);
        if(directory != null){
            return directory.find(path);
        }
        return fileSystem.find(path);
    }

    /**
     * 装载其他的文件系统到指定目录
     * @param path
     * @param system
     */
    public void loadFileSystem(String path, IFileSystem system) {
        if(exists(path)){
            throw new RuntimeException(String.format("need is a exists directory  not path: %s",path));
        }
        path = path.substring(0, path.length()-1);
        String parentPath = path.substring(0, path.lastIndexOf(IFileSystemConstants.PATH_SEPARATOR+1));
        if(!exists(parentPath)){
            try {
                mkdirs(parentPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        INode parentNode = find(parentPath);
        IFileSystemDirectory directory = new IFileSystemDirectory(parentNode, path, system);
        fileSystemDirectoriesOfPath.put(path, directory);
    }

    /**
     * 卸载文件系统
     * @param path
     */
    public void uninstallFileSystem(String path){
        fileSystemDirectoriesOfPath.remove(path);
    }

    @Override
    public IFileInputStream open(String filePath) throws IOException {
        IFileSystemDirectory directory = findFileSystemDirectory(filePath);
        if(directory != null){
            return directory.open(filePath);
        }
        return fileSystem.open(filePath);
    }

    @Override
    public IFileOutputStream append(String filePath) throws IOException {
        IFileSystemDirectory directory = findFileSystemDirectory(filePath);
        if(directory != null){
            return directory.append(filePath);
        }
        return fileSystem.append(filePath);
    }

    @Override
    public IFile createFile(String path) throws IOException {
        IFileSystemDirectory directory = findFileSystemDirectory(path);
        if(directory != null){
            return directory.createFile(path);
        }
        return fileSystem.createFile(path);
    }

    @Override
    public IDirectory mkdirs(String path) throws IOException {
        IFileSystemDirectory directory = findFileSystemDirectory(path);
        if(directory != null){
            return directory.mkdirs(path);
        }
        return fileSystem.mkdirs(path);
    }

    @Override
    public boolean exists(String path) {
        IFileSystemDirectory directory = findFileSystemDirectory(path);
        if(directory != null){
            return directory.exists(path);
        }
        return fileSystem.exists(path);
    }

    @Override
    public void delete(String path) {
        IFileSystemDirectory directory = findFileSystemDirectory(path);
        if(directory != null){
            directory.delete(path);
            if(fileSystemDirectoriesOfPath.containsKey(path)){
                fileSystemDirectoriesOfPath.remove(path);
            }
        }else {
            fileSystem.delete(path);
        }

    }

    @Override
    public void rename(String srcPath, String targetPath) throws IOException {
        IFileSystemDirectory directory = findFileSystemDirectory(srcPath);
        if(directory != null){
            directory.rename(srcPath, targetPath);
        }else {
            fileSystem.rename(srcPath, targetPath);
        }
    }

    @Override
    public void close() {
        fileSystem.close();
        fileSystemDirectoriesOfPath.clear();
    }

    /**
     * 判断路径是否在挂载的文件系统中 并返回对应的系统目录
     * @param path
     * @return
     */
    public IFileSystemDirectory findFileSystemDirectory(String path){
        if(fileSystemDirectoriesOfPath != null && !fileSystemDirectoriesOfPath.isEmpty()){
            for(String p : fileSystemDirectoriesOfPath.keySet()){
                if(path.startsWith(p)){
                    return fileSystemDirectoriesOfPath.get(p);
                }
            }
        }
        return null;
    }
}
