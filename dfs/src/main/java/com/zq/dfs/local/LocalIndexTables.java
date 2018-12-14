package com.zq.dfs.local;

import com.zq.dfs.INode;
import com.zq.dfs.IndexTables;

import java.io.File;

/**
 * @program: ZQDFS
 * @description: 本地索引目录
 * @author: zhouqi1
 * @create: 2018-12-11 19:18
 **/
public class LocalIndexTables implements IndexTables {

    @Override
    public INode root() {
        return null;
    }

    @Override
    public boolean exists(String path) {
        return new File(path).exists();
    }

    @Override
    public INode find(String path) {
        File file = new File(path);
        if(file.isDirectory()){
            return new ILocalDirectory(path);
        }
        return new ILocalFile(path);
    }

    @Override
    public void put(String path, INode node) {

    }

    @Override
    public void remove(String path) {

    }

    @Override
    public void clear() {

    }
}
