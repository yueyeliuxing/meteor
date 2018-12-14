package com.zq.dfs.data.storage;

import com.zq.dfs.constants.IFileSystemConstants;
import com.zq.dfs.data.DataIndexTables;

import java.io.*;

/**
 * @program: ZQDFS
 * @description: 镜像文件存储
 * @author: zhouqi1
 * @create: 2018-12-12 20:36
 **/
public class MirrorImageStorage {

    /**
     * 镜像文件名称
     */
    private static final String INDEX_TABLE_MIRROR_FILE_NAME = "mirror";

    private File mFile;

    private RandomAccessFile mirrorFile;

    public MirrorImageStorage(String indexTablesDirectory) {
        try {
            this.mFile = new File(indexTablesDirectory + IFileSystemConstants.PATH_SEPARATOR + INDEX_TABLE_MIRROR_FILE_NAME);
            this.mirrorFile = new RandomAccessFile(mFile, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DataIndexTables load2IndexTables(){
        DataIndexTables dataIndexTables = null;
        try {
            mirrorFile.seek(0);
            byte[] mirrorBytes = new byte[(int)mFile.length()];
            mirrorFile.read(mirrorBytes);
            dataIndexTables = new DataIndexTables();
            dataIndexTables.deserialize(mirrorBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dataIndexTables;
    }

    public void storageIndexTables(DataIndexTables indexTables){
        try {
            byte[] mirrorBytes = indexTables.serialize();
            mirrorFile.close();
            mFile.delete();
            this.mirrorFile = new RandomAccessFile(mFile, "rw");
            mirrorFile.write(mirrorBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close(){
        try {
            mirrorFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
