package com.zq.dfs;

import com.zq.dfs.config.Configuration;
import com.zq.dfs.config.FileSystemConfiguration;
import com.zq.dfs.config.LocatedConfiguration;
import com.zq.dfs.constants.FileSystemType;
import com.zq.dfs.data.IDataFileSystem;
import com.zq.dfs.local.ILocalFileSystem;

/**
 * @program: ZQDFS
 * @description: 文件系统创建
 * @author: zhouqi1
 * @create: 2018-12-12 11:47
 **/
public class IFileSystems {

    /**
     * 创建文件系统
     * @param configuration
     * @return
     */
    public static IFileSystem fileSystem(Configuration configuration){
        IFileSystem fileSystem = null;
        FileSystemConfiguration fileSystemConfig = new FileSystemConfiguration(configuration);
        FileSystemType type = fileSystemConfig.fileSystemType();
        switch (type){
            case LOCAL:
                fileSystem = new ILocalFileSystem();
                break;
            case MEMORY:
                fileSystem = new IDataFileSystem();
                break;
            case MEMORY_PERSISTENCE:
                fileSystem = new ILocatedDataFileSystem(new LocatedConfiguration(configuration));
                break;
            default:
                fileSystem = new ILocalFileSystem();
                break;
        }
        return fileSystem;
    }
}
