package com.zq.dfs;

/**
 * @program: ZQDFS
 * @description: 文件输入流
 * @author: zhouqi1
 * @create: 2018-12-11 10:56
 **/
public class IFileInputStream implements IDataInputStream {

    /**
     * 文件
     */
    private IFile file;

    public IFileInputStream(IFile file) {
        this.file = file;
    }

    @Override
    public byte read(){
        return file.read();
    }

    /**
     * 读取 数据
     * @param data
     */
    @Override
    public void read(byte[] data){
        file.read(data);
    }

    /**
     * 读取 数据
     * @param data
     */
    @Override
    public void read(byte[] data, int offset, int len){
        file.read(data, offset, len);
    }

    /**
     * 可读余量
     * @return
     */
    @Override
    public int available(){
        return file.available();
    }

    /**
     * 关闭
     */
    @Override
    public void close(){
        file.close();
    }
}
