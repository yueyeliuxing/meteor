package com.zq.dfs.data;


import com.zq.memory.pool.Chunk;
import com.zq.memory.pool.Mempool;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @program: ZQDFS
 * @description: 数据块
 * @author: zhouqi1
 * @create: 2018-12-11 15:39
 **/
public class DataBlock extends AbstractBlock implements Block {

    /**
     * 字节数组
     */
    protected Chunk dataChunk;

    public DataBlock(long blockId, int fileOffset) {
        this.blockId = blockId;
        this.offset = fileOffset;
        this.length = 0;
        this.dataChunk = Mempool.pool().alloc(8);
    }

    /**
     * 读取 数据
     * @param index
     */
    @Override
    public byte read(int index){
        return dataChunk.read(index);
    }

    /**
     * 读取 数据
     * @param data
     */
    @Override
    public void read(byte[] data, int offset, int len){
        int index = offset + len;
        rangeCheck(index);
        dataChunk.read(data, offset, len);
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
    @Override
    public int write(int offset, byte[] data, int len){
        if(offset > length){
            offset = length;
        }
        ensureExplicitCapacity(offset + len);
        dataChunk.write(offset, data, len);
        length += len;
        return length;
    }

    private void ensureExplicitCapacity(int minCapacity) {
        // overflow-conscious code
        if (minCapacity - dataChunk.chunkSize() > 0){
            grow(minCapacity);
        }
    }

    private void grow(int minCapacity) {
        // overflow-conscious code
        int oldCapacity = dataChunk.chunkSize();
        int newCapacity = oldCapacity + (oldCapacity >> 1);
        if (newCapacity - minCapacity < 0){
            newCapacity = minCapacity;
        }

        if (newCapacity - maxBlockSize > 0){
            newCapacity = hugeCapacity(minCapacity);
        }
        // minCapacity is usually close to size, so this is a win:
        this.dataChunk = Mempool.pool().alloc(newCapacity);
    }

    private int hugeCapacity(int minCapacity) {
        if (minCapacity < 0){
            // overflow
            throw new OutOfMemoryError();
        }
        return (minCapacity > maxBlockSize) ?
                Integer.MAX_VALUE :
                maxBlockSize;
    }
}
