package com.zq.dfs.data;

import com.zq.dfs.INode;

import java.util.Arrays;

/**
 * @program: ZQDFS
 * @description: 文件
 * @author: zhouqi1
 * @create: 2018-12-10 20:10
 **/
public class IArrayFile extends IDataFile {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 文件数据
     */
    private byte[] data;

    public IArrayFile(INode parent, String path, String name) {
        this.parent = parent;
        this.path = path;
        this.name = name;
        this.length = 0;
        this.data = new byte[8];
        this.readIndex = 0;
    }

    @Override
    public byte read(int index) {
        return data[index];
    }

    @Override
    public void doRead(byte[] data, int offset, int len) {
        System.arraycopy(this.data, offset, data, 0, len);
    }

    @Override
    public void doWrite(byte[] data, int offset, int len) {
        ensureExplicitCapacity(offset + len);
        System.arraycopy(data, 0, this.data, length, len);
    }

    private void ensureExplicitCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - data.length > 0){
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = data.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0){
            newCapacity = minCapacity;
        }

        if (newCapacity - MAX_ARRAY_SIZE > 0){
            newCapacity = hugeCapacity(minCapacity);
        }
        // minCapacity is usually close to size, so this is a win:
        data = Arrays.copyOf(data, newCapacity);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0){
            // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > MAX_ARRAY_SIZE) ?
                Integer.MAX_VALUE :
                MAX_ARRAY_SIZE;
    }

}
