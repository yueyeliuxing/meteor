package com.zq.dfs.local;

import com.zq.dfs.IAbstractNode;
import com.zq.dfs.IDirectory;
import com.zq.dfs.IFile;
import com.zq.dfs.INode;

import java.io.File;
import java.util.List;

/**
 * @program: ZQDFS
 * @description: 本地目录
 * @author: zhouqi1
 * @create: 2018-12-11 19:09
 **/
public class ILocalDirectory extends IAbstractNode implements IDirectory {

    private File file;

    public ILocalDirectory(String path) {
        this.path = path;
        this.file = new File(path);
        this.parent = new ILocalDirectory(file.getParent());
        this.name = file.getName();
    }

    @Override
    public void addDirectory(IDirectory directory) {

    }

    @Override
    public void addFile(IFile file) {

    }

    @Override
    public void remove(IDirectory directory) {

    }

    @Override
    public void remove(IFile file) {

    }

    @Override
    public boolean exists(IDirectory directory) {
        return false;
    }

    @Override
    public boolean exists(IFile file) {
        return false;
    }

    @Override
    public List<IDirectory> subdirectories() {
        return null;
    }

    @Override
    public List<IFile> subFiles() {
        return null;
    }

    @Override
    public boolean isDirectory() {
        return true;
    }
}
