package com.zq.memory.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: meteor
 * @description: 内存池
 * @author: zhouqi1
 * @create: 2018-12-17 16:47
 **/
public class Mempool {

    /**
     * subclass 列表
     */
    private List<SubClass> subClassList;

    /**
     * 默认 subclass 长度
     */
    private static final int DEFAULT_SUB_CLASS_LEN = 16;

    /**
     * 默认subclass内存大小
     */
    private static final int DEFAULT_SUB_CLASS_SIZE = 4*1024*1024;

    /**
     * 初始化块大小
     */
    private static final int FIRST_CHUNK_SIZE = 64;

    /**
     * 块大小增长因子
     */
    private static final float GROWTH_FACTOR = 1.25f;

    /**
     * 最后一个chunk的大小
     */
    private int lastChunkSize;

    private Mempool() {
        this.subClassList = new ArrayList<>(DEFAULT_SUB_CLASS_LEN);
        int subClassLen = this.subClassList.size();
        lastChunkSize = FIRST_CHUNK_SIZE;
        while (subClassLen >= 0){
            subClassList.add(new SubClass(DEFAULT_SUB_CLASS_SIZE, lastChunkSize));
            lastChunkSize = (int)(lastChunkSize * GROWTH_FACTOR);
            subClassLen--;
        }
    }
    public static class MempoolSingle {
        public static final Mempool MEM_POOL = new Mempool();
    }

    public static Mempool pool(){
        return MempoolSingle.MEM_POOL;
    }


    /**
     * 申请 chunk
     * @return
     */
    public Chunk alloc(int memSize){
        synchronized (subClassList){
            int subClassLen = subClassList.size();
            for(SubClass subClass : subClassList){
                int chunkSize = subClass.chunkSize();
                if(memSize <= chunkSize){
                    return subClass.alloc();
                }
            }
            while (memSize > lastChunkSize){
                lastChunkSize = (int)(lastChunkSize * GROWTH_FACTOR);
                subClassList.add(new SubClass(DEFAULT_SUB_CLASS_SIZE, lastChunkSize));
            }

            return subClassList.get(subClassLen-1).alloc();
        }
    }

    /**
     * chunk回收
     * @param chunk
     */
    public void recovery(Chunk chunk){
        chunk.subClass().recovery(chunk);
    }

    /**
     * 销毁
     */
    public void close(){
        subClassList.clear();
    }
}
