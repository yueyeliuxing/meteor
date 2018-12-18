package com.zq.dfs.data;

import com.zq.dfs.*;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @program: ZQDFS
 * @description: 目录
 * @author: zhouqi1
 * @create: 2018-12-10 20:10
 **/
public class IDataDirectory extends IAbstractNode implements IDirectory, Persistencer {

    /**
     * 子目录列表
     */
    private List<IDirectory> subdirectories;

    /**
     * 子文件列表
     */
    private List<IFile> subfiles;

    private Lock lock = new ReentrantLock();

    public IDataDirectory() {
    }

    public IDataDirectory(INode parent, String path) {
        this.parent = parent;
        this.path = path;
        this.name = extractName(path);
        this.subdirectories = new ArrayList<>();
        this.subfiles = new ArrayList<>();
        this.createTime = new Date();
    }

    @Override
    public boolean isDirectory() {
        return true;
    }

    /**
     * 添加目录
     * @param directory
     */
    @Override
    public void addDirectory(IDirectory directory){
        lock.lock();
        try{
            if(exists(directory)){
                remove(directory);
            }
            this.subdirectories.add(directory);
            setModifyTime(new Date());
        }finally {
            lock.unlock();
        }

    }

    /**
     * 添加文件
     * @param file
     */
    @Override
    public void addFile(IFile file){
        lock.lock();
        try{
            if(exists(file)){
                remove(file);
            }
            this.subfiles.add(file);
            setModifyTime(new Date());
        }finally {
            lock.unlock();
        }
    }


    /**
     * 删除指定目录
     * @param directory
     */
    @Override
    public void remove(IDirectory directory){
        lock.lock();
        try{
            if(!exists(directory)){
                return;
            }
            subdirectories.remove(directory);
            setModifyTime(new Date());
        }finally {
            lock.unlock();
        }

    }

    /**
     * 删除指定目录
     * @param file
     */
    @Override
    public void remove(IFile file){
        lock.lock();
        try{
            if(!exists(file)){
                return;
            }
            subfiles.remove(file);
            setModifyTime(new Date());
        }finally {
            lock.unlock();
        }

    }

    private void setModifyTime(Date date){
        this.lastModifyTime = date;
    }

    /**
     * 是否存在指定目录
     * @param directory
     * @return
     */
    @Override
    public boolean exists(IDirectory directory){
        return subdirectories.contains(directory);
    }

    /**
     * 是否存在指定文件
     * @param file
     * @return
     */
    @Override
    public boolean exists(IFile file){
        return subfiles.contains(file);
    }

    /**
     * 子目录
     * @return
     */
    @Override
    public List<IDirectory> subdirectories(){
        return Collections.unmodifiableList(subdirectories);
    }
    /**
     * 子目录
     * @return
     */
    @Override
    public List<IFile> subFiles(){
        return Collections.unmodifiableList(subfiles);
    }

    @Override
    public void distory() {
        for(IDirectory directory : subdirectories){
            directory.distory();
        }
        for(IFile file : subfiles){
            file.distory();
        }
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

        int directoryTotalSize = 0;
        int directorySize = 0;
        if(this.subdirectories != null){
            directorySize = this.subdirectories.size();
            for(IDirectory directory : subdirectories){
                String path = directory.path();
                directoryTotalSize += 4 + path.length();
            }
        }

        int fileTotalSize = 0;
        int fileSize = 0;
        if(this.subfiles != null){
            fileSize = this.subfiles.size();
            for(IFile file : subfiles){
                String path = file.path();
                fileTotalSize += 4 + path.length();
            }
        }
        int capacity = 4 + nameSize + 4 + pathSize + 8 + 8 + 4 + directoryTotalSize + 4 + fileTotalSize;
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

        byteBuffer.putInt(directorySize);
        if(directorySize > 0){
            for(IDirectory directory : subdirectories){
                String path = directory.path();
                byteBuffer.putInt(path.length());
                byteBuffer.put(path.getBytes());
            }
        }
        byteBuffer.putInt(fileSize);
        if(fileSize > 0){
            for(IFile file : subfiles){
                String path = file.path();
                byteBuffer.putInt(path.length());
                byteBuffer.put(path.getBytes());
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

        int directorySize = byteBuffer.getInt();
        if(directorySize > 0){
            for(int i = 0; i < directorySize; i++){
                int pSize = byteBuffer.getInt();
                byte[] path = new byte[pSize];
                byteBuffer.get(path);
                subdirectories.add(new IDataDirectory(this, new String(path)));
            }
        }

        int fileSize = byteBuffer.getInt();
        if(fileSize > 0){
            for(int i = 0; i < directorySize; i++){
                int pSize = byteBuffer.getInt();
                byte[] path = new byte[pSize];
                byteBuffer.get(path);
                subfiles.add(new IDataBlockFile(null, this, new String(path)));
            }
        }

    }
}
