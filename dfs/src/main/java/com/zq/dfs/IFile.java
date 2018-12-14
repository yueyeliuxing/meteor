package com.zq.dfs;

/**
 * @program: ZQDFS
 * @description: 文件
 * @author: zhouqi1
 * @create: 2018-12-10 20:10
 **/
public interface IFile extends INode{

    /**
     * 文件的长度
     * @return
     */
    int length();

    /**
     * 读文件
     * @return
     */
    byte read();

    /**
     * 读取 数据
     * @param data
     */
    void read(byte[] data);

    /**
     * 读取 数据
     * @param data
     */
    void read(byte[] data, int offset, int len);

    /**
     * 文件写数据
     * @param data
     * @return
     */
    int write(byte[] data);

    /**
     * 写文件
     * @param offset
     * @param data
     * @return
     */
    int write(int offset, byte[] data);

    /**
     * 可读余量
     * @return
     */
    int available();

}
