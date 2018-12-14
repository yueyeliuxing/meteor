package com.zq.dfs.data.storage;

import com.zq.dfs.constants.IFileSystemConstants;
import com.zq.dfs.data.Block;
import com.zq.dfs.data.BlockPool;
import com.zq.dfs.data.DataBlock;
import com.zq.dfs.data.DataBlockPool;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @program: ZQDFS
 * @description: 存储的数据块池
 * @author: zhouqi1
 * @create: 2018-12-13 10:28
 **/
public class LocatedDataBlockPool extends DataBlockPool implements BlockPool {

    /**
     * 块目录文件
     */
    private File blockDirectory;

    public LocatedDataBlockPool(String blockDirectoryPath) {
        this.blockDirectory = new File(blockDirectoryPath);
        //初始化块
        initBlocks();

    }

    private void initBlocks(){
        if(!blockDirectory.exists()){
            blockDirectory.mkdirs();
        }
        File[] blockFiles = blockDirectory.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.startsWith(DataBlock.BLOCK_NAME_PREX);
            }
        });
        if(blockFiles != null && blockFiles.length > 0){
            for(File file : blockFiles){
                addBlock(new LocatedDataBlock(file));
            }
        }
    }

    @Override
    public Block newBlock(long blockId, int fileOffset) {
        return new LocatedDataBlock(new File(getBlockFilePath(blockId, fileOffset)));
    }

    private String getBlockFilePath(long blockId, int fileOffset) {
        return String.format("%s-%s", blockDirectory + IFileSystemConstants.PATH_SEPARATOR + DataBlock.BLOCK_NAME_PREX+blockId, fileOffset);
    }

    @Override
    public Block remove(long blockId) {
        LocatedDataBlock block = (LocatedDataBlock)super.remove(blockId);
        block.deleteLocated();
        return block;
    }

    @Override
    public void close() {
        super.close();
        blockDirectory = null;
    }
}
