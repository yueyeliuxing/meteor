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
     * 文件索引目录地址
     * @return
     */
    public String indexTableFilePath(){
        return configuration.getParam(IFileSystemConstants.INDEX_TABLES_FILE_PATH);
    }

    /**
     * 块存储地址
     * @return
     */
    public String blockFilePath(){
        return configuration.getParam(IFileSystemConstants.BLOCK_FILE_PATH);
    }
}
