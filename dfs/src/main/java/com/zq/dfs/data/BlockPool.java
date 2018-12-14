package com.zq.dfs.data;

/**
 * @program: ZQDFS
 * @description: 块池
 * @author: zhouqi1
 * @create: 2018-12-12 15:30
 **/
public interface BlockPool {

    /**
     * 查找指定ID的数据块
     * @param blockId
     * @return
     */
    Block find(long blockId);

    /**
     * 申请一个空白的数据块
     * @return
     */
    Block apply(int fileOffset);

    /**
     *
     * 销毁指定ID的数据块
     * @param blockId
     */
    Block remove(long blockId);

    /**
     * 关闭
     */
    void close();
}
