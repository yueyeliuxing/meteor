package com.zq.dfs.data.storage;

import com.zq.dfs.data.Block;
import com.zq.dfs.data.DataBlock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: ZQDFS
 * @description: 定位的数据块
 * @author: zhouqi1
 * @create: 2018-12-13 10:54
 **/
public class LocatedDataBlock extends DataBlock implements Block{

    /**
     * 块文件路径
     */
    private File file;

    /**
     * 块文件
     */
    private RandomAccessFile blockFile;

    /**
     * 块同步线程
     */
    private ExecutorService blockSynchronizedThread;

    public LocatedDataBlock(File file) {
        super(Long.parseLong(file.getName().split("-")[0].substring(BLOCK_NAME_PREX.length())),
                Integer.parseInt(file.getName().split("-")[1]));
        try {
            this.file = file;
            this.blockFile = new RandomAccessFile(file, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.blockSynchronizedThread = Executors.newSingleThreadExecutor();

        /**
         * 初始化块数据
         */
        initBlockData();
    }

    private void initBlockData() {
        try {
            this.length = (int) blockFile.length();
            if(this.length > 0){
                blockFile.seek(0L);
                byte[] data = new byte[this.length];
                blockFile.read(data);
                this.data = data;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int write(int offset, byte[] data, int len) {
        blockSynchronizedThread.execute(()->{
            try {
                blockFile.seek(blockFile.length());
                blockFile.write(data, offset, len);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        return super.write(offset, data, len);
    }

    @Override
    public void close() {
        try {
            super.close();
            blockSynchronizedThread.shutdownNow();
            blockFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除对应的存储 文件
     */
    public void deleteLocated(){
        file.delete();
    }
}
