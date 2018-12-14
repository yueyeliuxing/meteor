package com.zq.dfs.data;

import com.zq.dfs.util.IdGenerator;
import com.zq.dfs.util.SnowFlakeIdGenerator;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ZQDFS
 * @description: 数据块池
 * @author: zhouqi1
 * @create: 2018-12-12 15:38
 **/
public class DataBlockPool implements BlockPool {

    /**
     * id生成器
     */
    private IdGenerator idGenerator;

    private Map<Long, Block> blocksOfId;

    public DataBlockPool() {
        blocksOfId = new HashMap<>();
        idGenerator = new SnowFlakeIdGenerator(0, 0);
    }

    @Override
    public Block find(long blockId) {
        return blocksOfId.get(blockId);
    }

    @Override
    public Block apply(int fileOffset) {
        long blockId = idGenerator.nextId();
        Block block = newBlock(blockId, fileOffset);
        blocksOfId.put(blockId, block);
        return block;
    }

    protected Block newBlock(long blockId, int fileOffset) {
        return new DataBlock(blockId, fileOffset);
    }

    /**
     * 向块池中添加数据块
     * @param block
     */
    protected void addBlock(Block block){
        this.blocksOfId.put(block.id(), block);
    }

    @Override
    public Block remove(long blockId) {
        Block block = find(blockId);
        if(block != null){
            block.close();
            blocksOfId.remove(blockId);
        }
        return block;
    }

    @Override
    public void close() {
        if(blocksOfId != null && !blocksOfId.isEmpty()){
            for(Block block : blocksOfId.values()){
                block.close();
            }
            blocksOfId.clear();
        }
    }
}
