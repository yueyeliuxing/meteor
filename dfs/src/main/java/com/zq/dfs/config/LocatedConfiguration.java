package com.zq.dfs.config;

import com.zq.dfs.constants.IFileSystemConstants;

/**
 * @program: ZQDFS
 * @description: 存储配置
 * @author: zhouqi1
 * @create: 2018-12-13 15:33
 **/
public class LocatedConfiguration extends FileSystemConfiguration {

    public LocatedConfiguration(Configuration configuration) {
        super(configuration);
    }

    /**
     * 文件系统存储根目录
     * @return
     */
    public String fsStorageRootPath(){
        return configuration.get(IFileSystemConstants.FS_STORAGE_ROOT_PATH);
    }
}
