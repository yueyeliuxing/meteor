package com.zq.dfs.data;

import com.zq.dfs.INode;
import com.zq.dfs.constants.IFileSystemConstants;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @program: ZQDFS
 * @description: 文件
 * @author: zhouqi1
 * @create: 2018-12-10 20:10
 **/
public class IDataBlockFile extends IDataFile {

    /**
     * 块池
     */
    private BlockPool blockPool;

    /**
     * 数据块
     */
    private List<Block> blocks;

    public IDataBlockFile() {
    }

    public IDataBlockFile(BlockPool blockPool) {
        this.blockPool = blockPool;
    }

    public IDataBlockFile(BlockPool blockPool, INode parent, String path) {
        this.blockPool = blockPool;
        this.parent = parent;
        this.path = path;
        this.name = getName(path);
        this.length = 0;
        this.readIndex = 0;
        this.blocks = new ArrayList<>();
    }

    private String getName(String path) {
        return path.substring(path.lastIndexOf(IFileSystemConstants.PATH_SEPARATOR)+1, path.length());
    }

    @Override
    public byte read(int index) {
        for(Block block : blocks){
            if(block.valid(index)){
                return block.read(block.blockIndex(index));
            }
        }
        return -1;
    }

    @Override
    public void doRead(byte[] data, int offset, int len) {
        for(Block block : blocks){
            if(block.valid(offset)){
                int blockOffset = block.blockIndex(offset);
                int available = block.available(offset);
                int l = Math.min(len, available);
                block.read(data, blockOffset, l);
                len -= available;
                offset += l;
            }
        }
    }

    @Override
    public void distory() {
        for(Block block : blocks){
            blockPool.remove(block.id());
        }
    }

    @Override
    public void close() {
        super.close();
        if(blocks != null && !blocks.isEmpty()){
            for(Block block : blocks){
                block.close();
            }
            blocks.clear();
        }
    }

    @Override
    public void doWrite(byte[] data, int offset, int len) {
        for(Block block : blocks) {
            if (block.valid(offset)) {
                int blockOffset = block.blockIndex(offset);
                int writeable = block.writeable();
                int l = Math.min(len, writeable);
                block.write(blockOffset, data, l);
                if(len <= writeable){
                    return;
                }
                len -= writeable;
                offset += l;
            }
        }
        while(len > 0){
            Block block = newBlock(offset);
            int blockOffset = block.blockIndex(offset);
            int writeable = block.writeable();
            int l = Math.min(len, writeable);
            block.write(blockOffset, data, l);
            len -= writeable;
            offset += l;

            blocks.add(block);
        }
    }

    private Block newBlock(int fileOffset) {
        return blockPool.apply(fileOffset);
    }

    @Override
    public byte[] serialize() {
        int nameSize = 0;
        byte[] nameBytes = null;
        if(this.name != null){
            nameSize = this.name.length();
            nameBytes = this.name.getBytes();
        }

        int pathSize = 0;
        byte[] pathBytes = null;
        if(this.path != null){
            pathSize = this.path.length();
            pathBytes = this.path.getBytes();
        }

        long creativeTime = 0;
        if(this.createTime != null){
            creativeTime = this.createTime.getTime();
        }

        long lastModifyTime = 0;
        if(this.lastModifyTime != null){
            lastModifyTime = this.lastModifyTime.getTime();
        }

        int length = this.length;

        int readIndex = this.readIndex;

        int blockSize = 0;
        if(this.blocks != null){
            blockSize = this.blocks.size();
        }


        int capacity = 4 + nameSize + 4 + pathSize + 8 + 8 + 4 + 4 + 4 + blockSize * 8;
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
        byteBuffer.putInt(nameSize);
        if(nameSize > 0){
            byteBuffer.put(nameBytes);
        }

        byteBuffer.putInt(pathSize);
        if(pathSize > 0){
            byteBuffer.put(pathBytes);
        }

        byteBuffer.putLong(creativeTime);
        byteBuffer.putLong(lastModifyTime);
        byteBuffer.putInt(length);
        byteBuffer.putInt(readIndex);

        byteBuffer.putInt(blockSize);
        if(blockSize > 0){
            for(Block block : blocks){
                byteBuffer.putLong(block.id());
            }
        }

        return byteBuffer.array();
    }

    @Override
    public void deserialize(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        int nameSize = byteBuffer.getInt();
        if(nameSize > 0){
            byte[] name = new byte[nameSize];
            byteBuffer.get(name);
            this.name = new String(name);
        }

        int pathSize = byteBuffer.getInt();
        if(pathSize > 0){
            byte[] path = new byte[pathSize];
            byteBuffer.get(path);
            this.path = new String(path);
        }

        long createTime = byteBuffer.getLong();
        if(createTime > 0){
            this.createTime = new Date(createTime);
        }

        long lastModifyTime = byteBuffer.getLong();
        if(lastModifyTime > 0){
            this.lastModifyTime = new Date(lastModifyTime);
        }

        int length = byteBuffer.getInt();
        this.length = length;

        int readIndex = byteBuffer.getInt();
        this.readIndex = readIndex;

        int blockSize = byteBuffer.getInt();
        if(blockSize > 0){
            List<Block> blocks = new ArrayList<>();
            for(int i = 0; i < blockSize; i++){
                long blockId = byteBuffer.getLong();
                blocks.add(blockPool.find(blockId));
            }
        }
    }
}
