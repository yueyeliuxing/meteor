package com.zq.dfs;

/**
 * @program: ZQDFS
 * @description: 索引目录
 * @author: zhouqi1
 * @create: 2018-12-11 14:22
 **/
public interface IndexTables {

    /**
     * 根节点
     * @return
     */
    INode root();

    /**
     * 路径是否存在
     * @param path
     * @return
     */
    boolean exists(String path);

    /**
     * 查找路径对应的节点
     * @param path
     * @return
     */
    INode find(String path);

    /**
     * 添加路径对应的节点
     * @param path
     * @param node
     */
    void put(String path, INode node);

    /**
     * 删除对应路径的 节点
     * @param path
     */
    void remove(String path);

    /**
     * 关闭
     */
    void close();
}
