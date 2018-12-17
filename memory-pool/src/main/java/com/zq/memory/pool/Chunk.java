package com.zq.memory.pool;

import java.util.Arrays;

/**
 * @program: meteor
 * @description: 用于缓存记录的内存空间
 * @author: zhouqi1
 * @create: 2018-12-17 16:14
 **/
public class Chunk {

    /**
     * 属于的subclass
     */
    private SubClass subClass;

    /***
     * 块的长度
     */
    private int length;

    /**
     * 内存块
     */
    private byte[] data;

    public Chunk(SubClass subClass, int chunkSize) {
        this.subClass = subClass;
        this.length = 0;
        this.data = new byte[chunkSize];
    }

    /**
     * 返回所属的subclass
     * @return
     */
    public SubClass subClass(){
        return subClass;
    }

    /**
     * 读取 数据
     * @param index
     */
    public byte read(int index){
        return data[index];
    }

    /**
     * 读取 数据
     * @param data
     */
    public void read(byte[] data, int offset, int len){
        int index = offset + len;
        rangeCheck(index);
        System.arraycopy(this.data, offset, data, 0, len);
    }

    private void rangeCheck(int index) {
        if (index > length){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Length: "+length;
    }

    /**
     * 写数据
     * @param offset
     * @param data
     * @return
     */
    public int write(int offset, byte[] data, int len){
        if(offset > length){
            offset = length;
        }
        if(offset + len - data.length > 0){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(offset + len));
        }
        System.arraycopy(data, 0, this.data, length, len);
        length += len;
        return length;
    }

    /**
     * 还原成空的内存块
     */
    public void recover(){
        this.length = 0;
    }
}
