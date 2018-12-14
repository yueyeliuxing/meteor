package com.zq.dfs.constants;

/**
 * @program: ZQDFS
 * @description: 文件系统类型
 * @author: zhouqi1
 * @create: 2018-12-12 11:40
 **/
public enum FileSystemType {
    /**
     * 本地文件系统
     */
    LOCAL,

    /**
     * 内存文件系统
     */
    MEMORY,

    /**
     *  内存文件系统 -- 可持久化
     */
    MEMORY_PERSISTENCE,

}
