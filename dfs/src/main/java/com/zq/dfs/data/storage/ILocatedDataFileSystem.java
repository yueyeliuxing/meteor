package com.zq.dfs.data.storage;

import com.zq.dfs.IFileSystem;
import com.zq.dfs.config.LocatedConfiguration;
import com.zq.dfs.data.IDataFileSystem;
import com.zq.dfs.local.LocalIndexTables;

/**
 * @program: ZQDFS
 * @description: 本地存储的文件系统
 * @author: zhouqi1
 * @create: 2018-12-13 15:32
 **/
public class ILocatedDataFileSystem extends IDataFileSystem implements IFileSystem {

    public ILocatedDataFileSystem(LocatedConfiguration locatedConfiguration){
        this.blockPool = new LocatedDataBlockPool(locatedConfiguration.fsStorageRootPath());
        this.indexTables = new LocatedDataIndexTables(locatedConfiguration.fsStorageRootPath(), this.blockPool);
    }
}
