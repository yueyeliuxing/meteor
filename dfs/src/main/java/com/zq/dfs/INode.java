package com.zq.dfs;

import java.util.Date;

/**
 * @program: ZQDFS
 * @description: 节点
 * @author: zhouqi1
 * @create: 2018-12-10 20:11
 **/
public interface INode extends Persistencer {

    /**
     * 父级
     * @return
     */
    void parent(INode parent);

    /**
     * 父级
     * @return
     */
    INode parent();

    /**
     * 名称
     * @return
     */
    void name(String name);

    /**
     * 名称
     * @return
     */
    String name();

    /**
     *  路径
     * @return
     */
    String path();

    /**
     * 设置路径
     * @return
     */
    void path(String path);

    /**
     * 是否是目录
     * @return
     */
    boolean isDirectory();

    /**
     * 返回创建时间
     * @return
     */
    Date createTime();

    /**
     * 最后修改时间
     * @return
     */
    Date lastModifyTime();

    /**
     * 销毁
     */
    void distory();

    /**
     * 销毁
     */
    void close();

}
