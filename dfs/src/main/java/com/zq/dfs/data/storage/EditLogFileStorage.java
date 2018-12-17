package com.zq.dfs.data.storage;

import com.zq.dfs.constants.IFileSystemConstants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @program: ZQDFS
 * @description: 更改日志存储
 * @author: zhouqi1
 * @create: 2018-12-12 20:38
 **/
public class EditLogFileStorage {

    /**
     * 操作日志名称
     */
    private static final String INDEX_TABLE_EDIT_LOG_FILE_NAME = "edit";
    private static final String INDEX_TABLE_EDIT_LOG_NEW__FILE_NAME = "edit.new";

    private volatile boolean point = false;

    private String logFilePath;

    private String logNewFilePath;

    private volatile RandomAccessFile logFile;

    private volatile RandomAccessFile logNewFile;

    private Object fileSwitchTag = new Object();

    public EditLogFileStorage(String indexTablesDirectory) {
        try {
            this.logFilePath = indexTablesDirectory + IFileSystemConstants.PATH_SEPARATOR + INDEX_TABLE_EDIT_LOG_FILE_NAME;
            this.logNewFilePath = indexTablesDirectory + IFileSystemConstants.PATH_SEPARATOR + INDEX_TABLE_EDIT_LOG_NEW__FILE_NAME;
            this.logFile = new RandomAccessFile(logFilePath, "rw");
            this.logNewFile = new RandomAccessFile(logNewFilePath, "rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void storage(EditLog editLog){
        synchronized (fileSwitchTag){
            try {
                RandomAccessFile file = getEditLogFile();
                byte[] logValue = editLog.serialize();
                file.writeInt(logValue.length);
                file.write(logValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private RandomAccessFile getEditLogFile() {
        return  point ? logNewFile : logFile;
    }

    public EditLog acquire(){
        EditLog editLog = new EditLog();
        try {
            RandomAccessFile file = getEditLogFile();
            int logValueSize = file.readInt();
            byte[] logValue = new byte[logValueSize];
            file.read(logValue);
            editLog.deserialize(logValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return editLog;
    }

    public int readable(){
        try {
            return (int)(logFile.length() - logFile.getFilePointer());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 是否可以开启检查点
     * @return
     */
    public boolean isStartCheckpoint(){
        try {
            return logFile.length() >= 64 * 1024;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 开启检查点
     */
    public void startCheckpoint(){
        this.point = true;
        try {
            logFile.seek(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开启检查点
     */
    public void endCheckpoint(){
        synchronized (fileSwitchTag){
            try{
                logFile.close();
                new File(logFilePath).delete();
                long pointer = logNewFile.getFilePointer();
                logNewFile.close();
                new File(logNewFilePath).renameTo(new File(logFilePath));
                logFile = new RandomAccessFile(logFilePath, "rw");
                logFile.seek(pointer);
                logNewFile = new RandomAccessFile(logNewFilePath, "rw");
                this.point = false;
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }

    public void close(){
        try {
            logFile.close();
            logNewFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
