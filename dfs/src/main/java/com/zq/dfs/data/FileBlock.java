package com.zq.dfs.data;

import com.zq.dfs.constants.IFileSystemConstants;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @program: ZQDFS
 * @description: 数据块
 * @author: zhouqi1
 * @create: 2018-12-11 15:39
 **/
public class FileBlock extends AbstractBlock implements Block {

    /**
     * 块所在的目录路径
     */
    private String blockDirectoryPath;

    /**
     * 块文件
     */
   private RandomAccessFile blockFile;

    public FileBlock(int offset, long blockId) {
        this.offset = offset;
        this.length = 0;
        this.blockId = blockId;
        try {
            this.blockFile = new RandomAccessFile(blockDirectoryPath+ IFileSystemConstants.PATH_SEPARATOR+this.name(), "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int length() {
        try {
            return (int)blockFile.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 读取 数据
     * @param index
     */
    @Override
    public byte read(int index){
        try {
            blockFile.seek(index);
            return blockFile.readByte();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return -1;
    }

    /**
     * 读取 数据
     * @param data
     */
    @Override
    public void read(byte[] data, int offset, int len){
        try {
            blockFile.read(data, offset, len);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 写数据
     * @param offset
     * @param data
     * @return
     */
    @Override
    public int write(int offset, byte[] data, int len){
        try {
            blockFile.write(data, offset, len);
            return (int)blockFile.getFilePointer();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
