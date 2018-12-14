package com.zq.dfs.config;

/**
 * @program: ZQDFS
 * @description: 可配置的
 * @author: zhouqi1
 * @create: 2018-12-12 11:30
 **/
public interface Configurable {

    /**
     * 设置配置项
     * @param configuration
     */
    void setConfiguration(Configuration configuration);
}
