package com.zq.dfs.data;

public interface Block {

    /**
     * 块ID
     * @return
     */
    long id();

    /**
     * 块名称
     * @return
     */
    String name();

    /**
     * 数据长度
     * @return
     */
    int length();

    /**
     * 返回文件的偏移量
     * @return
     */
    int offset();

    /**
     * 转换文件位置在块中的偏移量
     * @param fileIndex
     * @return
     */
    int blockIndex(int fileIndex);

    /**
     * 相对文件位置 块剩余可的数据长度
     * @param index
     * @return
     */
    int available(int index);

    /**
     * 文件位置是否在当前块中
     * @param index
     * @return
     */
    boolean valid(int index);

    /**
     * 读数据
     * @param index
     * @return
     */
    byte read(int index);

    /**
     * 读数据
     * @param data
     * @param offset
     * @param len
     */
    void read(byte[] data, int offset, int len);

    /**
     * 写数据
     * @param offset
     * @param data
     * @param len
     * @return
     */
    int write(int offset, byte[] data, int len);

    /**
     * 块剩余可写的数据大小
     * @return
     */
    int writeable();

    /**
     * 销毁
     * @return
     */
    void close();
}
