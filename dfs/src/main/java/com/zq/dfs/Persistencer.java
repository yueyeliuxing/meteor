package com.zq.dfs;

/**
 * @program: ZQDFS
 * @description: 持久化器接口
 * @author: zhouqi1
 * @create: 2018-12-12 16:03
 **/
public interface Persistencer {

    /**
     * 序列化
     * @return
     */
    byte[] serialize();

    /**
     * 反序列化
     * @return
     */
    void deserialize(byte[] data);
}
