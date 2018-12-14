package com.zq.dfs.data;

/**
 * @program: ZQDFS
 * @description: 数据块
 * @author: zhouqi1
 * @create: 2018-12-11 15:39
 **/
public abstract class AbstractBlock implements Block {

    public static final String BLOCK_NAME_PREX = "bl_";

    /**
     * 块ID
     */
    protected long blockId;

    /**
     * 块大小
     */
    protected int maxBlockSize = 64 * 1024;

    /**
     * 所属文件中的偏移量
     */
    protected int offset;

    /**
     * 数据长度
     */
    protected int length;

    @Override
    public long id() {
        return blockId;
    }

    @Override
    public String name() {
        return BLOCK_NAME_PREX+blockId;
    }

    @Override
    public int length(){
        return length;
    }

    @Override
    public int offset() {
        return offset;
    }

    /**
     * 文件的索引在数据块中的位置
     * @param fileIndex
     * @return
     */
    @Override
    public int blockIndex(int fileIndex){
        return fileIndex - offset;
    }

    /**
     * 可读余量
     * @return
     */
    @Override
    public int available(int index){
        return length() - blockIndex(index);
    }

    /**
     * 是否有效
     * @param index
     * @return
     */
    @Override
    public boolean valid(int index){
        return index >= offset && index <= offset + length();
    }

    /**
     * 读取 数据
     * @param index
     */
    @Override
    public abstract byte read(int index);

    /**
     * 读取 数据
     * @param data
     */
    @Override
    public abstract void read(byte[] data, int offset, int len);

    /**
     * 写数据
     * @param offset
     * @param data
     * @return
     */
    @Override
    public abstract int write(int offset, byte[] data, int len);

    /**
     * 可写入数据容量
     * @return
     */
    @Override
    public int writeable(){
        return maxBlockSize - length();
    }

    @Override
    public void close() {
    }
}
