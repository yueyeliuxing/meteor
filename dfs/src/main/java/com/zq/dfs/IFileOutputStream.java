package com.zq.dfs;

/**
 * @program: ZQDFS
 * @description: 文件输出流
 * @author: zhouqi1
 * @create: 2018-12-11 11:09
 **/
public class IFileOutputStream implements IDataOutputStream {

    /**
     * 文件
     */
    private IFile file;

    public IFileOutputStream(IFile file) {
        this.file = file;
    }

    /**
     * 文件写数据
     * @param data
     * @return
     */
    @Override
    public int write(byte[] data){
        return file.write(data);
    }

    /**
     * 文件写数据
     * @param offset
     * @param data
     * @return
     */
    @Override
    public int write(int offset, byte[] data){
        return file.write(offset, data);
    }

    /**
     * 关闭
     */
    @Override
    public void close(){
        file.close();
    }
}
