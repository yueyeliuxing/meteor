package com.zq.dfs.data;

import com.zq.dfs.IAbstractNode;
import com.zq.dfs.IFile;

/**
 * @program: ZQDFS
 * @description: 文件
 * @author: zhouqi1
 * @create: 2018-12-10 20:10
 **/
public abstract class IDataFile extends IAbstractNode implements IFile {

    /**
     * 文件的大小
     */
    protected int length;

    /**
     * 读索引
     */
    protected int readIndex;

    @Override
    public boolean isDirectory() {
        return false;
    }

    /**
     * 文件的长度
     * @return
     */
    @Override
    public int length(){
        return length;
    }

    /**
     * 读文件
     * @return
     */
    @Override
    public byte read(){
        int index = readIndex + 1;
        rangeCheck(index);
        readIndex++;
        return read(index);
    }

    /**
     * 读指定索引的字节
     * @param index
     * @return
     */
    public abstract byte read(int index);

    /**
     * 读取 数据
     * @param data
     */
    @Override
    public void read(byte[] data){
        read(data, readIndex, data.length);
    }

    /**
     * 读取 数据
     * @param data
     */
    @Override
    public void read(byte[] data, int offset, int len){
        int index = offset + len;
        rangeCheck(index);
        doRead(data, offset, len);
        readIndex = index;
    }

    /**
     * 读取数据
     * @param data
     * @param offset
     * @param len
     */
    public abstract void doRead(byte[] data,  int offset, int len);

    private void rangeCheck(int index) {
        if (index >= length){
            throw new IndexOutOfBoundsException(outOfBoundsMsg(index));
        }
    }

    private String outOfBoundsMsg(int index) {
        return "Index: "+index+", Length: "+length;
    }

    /**
     * 文件写数据
     * @param data
     * @return
     */
    @Override
    public int write(byte[] data){
        return write(length, data);
    }

    /**
     * 写文件
     * @param offset
     * @param data
     * @return
     */
    @Override
    public int write(int offset, byte[] data){
        if(offset > length){
            offset = length;
        }
        int numNew = data.length;
        doWrite(data, offset, numNew);
        length += numNew;
        return length;
    }

    /**
     * 数据写入
     * @param data
     * @param offset
     * @param len
     */
    public abstract void doWrite(byte[] data, int offset, int len);

    /**
     * 可读余量
     * @return
     */
    @Override
    public int available(){
        return length - readIndex;
    }

    /**
     * 关闭
     */
    @Override
    public void close(){
        readIndex = 0;
    }

}
