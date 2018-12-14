package com.zq.dfs.local;

import com.zq.dfs.IAbstractNode;
import com.zq.dfs.IFile;
import com.zq.dfs.INode;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @program: ZQDFS
 * @description: 文件
 * @author: zhouqi1
 * @create: 2018-12-10 20:10
 **/
public class ILocalFile extends IAbstractNode implements IFile {

    private RandomAccessFile file;

    public ILocalFile(String path) {
        try{
            this.file = new RandomAccessFile(path, "rw");
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @Override
    public int length() {
        try{
            return (int)file.length();
        }catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public byte read() {
        try{
            return (byte)file.read();
        }catch (IOException e){
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void read(byte[] data) {
        try{
            file.read(data);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void read(byte[] data, int offset, int len) {
        try{
            file.read(data, offset, len);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public int write(byte[] data) {
        try {
            file.write(data);
            return (int)file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int write(int offset, byte[] data) {
        try {
            file.write(data, offset, data.length);
            return (int)file.length();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public int available() {
        try {
            return (int)(file.length() - file.getFilePointer());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public void close() {
        try {
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isDirectory() {
        return false;
    }
}
