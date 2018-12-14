package com.zq.dfs.util;

/**
 * 分布式ID生成器
 */
public interface IdGenerator {
    /**
     * 获取分布式ID
     * @return
     */
    long nextId();
}
