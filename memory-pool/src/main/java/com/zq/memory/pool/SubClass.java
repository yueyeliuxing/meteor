package com.zq.memory.pool;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: meteor
 * @description: Chunk管理
 * @author: zhouqi1
 * @create: 2018-12-17 16:35
 **/
public class SubClass {

    /**
     * subclass 总内存大小
     */
    private int subClassSize;

    /**
     * 单个chunk 内存大小
     */
    private int chunkSize;

    /**
     * 未使用的chunk集合
     */
    private List<Chunk> unusedChunks;

    /**
     * 已使用的chunk集合
     */
    private List<Chunk> usedChunks;

    public SubClass(int subClassSize, int chunkSize) {
        this.subClassSize = subClassSize;
        this.chunkSize = chunkSize;
        this.unusedChunks = new ArrayList<>();
        this.usedChunks  = new ArrayList<>();
        initUnusedChunks();
    }

    void initUnusedChunks(){
        int unusedChunksLen = this.subClassSize / chunkSize;
        while (unusedChunksLen >= 0){
            unusedChunksLen--;
            this.unusedChunks.add(new Chunk(this, this.chunkSize));
        }
    }

    public int chunkSize(){
        return chunkSize;
    }

    /**
     * 申请 chunk
     * @return
     */
    public Chunk alloc(){
        Chunk chunk = unusedChunks.remove(0);
        usedChunks.add(chunk);
        return chunk;
    }

    /**
     * chunk回收
     * @param chunk
     */
    public void recovery(Chunk chunk){
        chunk.recover();
        usedChunks.remove(chunk);
        unusedChunks.add(chunk);
    }

    /**
     * 销毁
     */
    public void close(){
        unusedChunks.clear();
        usedChunks.clear();
    }
}
