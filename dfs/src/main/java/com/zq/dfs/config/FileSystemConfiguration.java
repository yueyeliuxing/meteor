package com.zq.dfs.config;

import com.zq.dfs.constants.FileSystemType;
import com.zq.dfs.constants.IFileSystemConstants;

/**
 * @program: ZQDFS
 * @description: 文件系统配置
 * @author: zhouqi1
 * @create: 2018-12-12 11:36
 **/
public class FileSystemConfiguration {

    /**
     * 配置
     */
    protected Configuration configuration;

    public FileSystemConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    /**
     * 文件系统类型
     * @return
     */
    public FileSystemType fileSystemType(){
        return FileSystemType.valueOf(configuration.getParam(IFileSystemConstants.FILE_SYSTEM_TYPE));
    }
}
