package com.zq.dfs.data;

import com.zq.dfs.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class IFileSystemDirectory extends IAbstractNode implements IDirectory, IFileSystem {

    private IFileSystem fileSystem;

    public IFileSystemDirectory(INode parent, String path, IFileSystem fileSystem) {
        this.parent = parent;
        this.path = path;
        this.name = extractName(path);
        this.fileSystem = fileSystem;
        this.createTime = new Date();
        this.fileSystem = fileSystem;
    }

    @Override
    public void addDirectory(IDirectory directory) {
        root().addDirectory(directory);
    }

    @Override
    public void addFile(IFile file) {
        root().addFile(file);
    }

    @Override
    public void remove(IDirectory directory) {
        root().remove(directory);
    }

    @Override
    public void remove(IFile file) {
        root().remove(file);
    }

    @Override
    public boolean exists(IDirectory directory) {
        return root().exists(directory);
    }

    @Override
    public boolean exists(IFile file) {
        return root().exists(file);
    }

    @Override
    public List<IDirectory> subdirectories() {
        return root().subdirectories();
    }

    @Override
    public List<IFile> subFiles() {
        return root().subFiles();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    @Override
    public IDirectory root() {
        return fileSystem.root();
    }

    @Override
    public INode find(String path) {
        return fileSystem.find(path);
    }

    @Override
    public IFileInputStream open(String filePath) throws IOException {
        return fileSystem.open(extractTargetPath(filePath));
    }

    @Override
    public IFileOutputStream append(String filePath) throws IOException {
        return fileSystem.append(extractTargetPath(filePath));
    }

    @Override
    public IFile createFile(String path) throws IOException {
        return fileSystem.createFile(extractTargetPath(path));
    }

    @Override
    public IDirectory mkdirs(String path) throws IOException {
        return fileSystem.mkdirs(extractTargetPath(path));
    }

    @Override
    public boolean exists(String path) {
        return fileSystem.exists(extractTargetPath(path));
    }

    @Override
    public void delete(String path) {
        fileSystem.delete(extractTargetPath(path));
    }

    @Override
    public void rename(String srcPath, String targetPath) throws IOException {
        rename(extractTargetPath(srcPath), extractTargetPath(targetPath));
    }

    /**
     * 从路径中提取目标路径
     * @param path
     * @return
     */
    private String extractTargetPath(String path){
        return path.substring(path().length()-1);
    }
}
