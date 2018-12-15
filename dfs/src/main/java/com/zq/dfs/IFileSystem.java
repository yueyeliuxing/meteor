package com.zq.dfs;

import com.zq.dfs.config.Configurable;

import java.io.IOException;

/**
 * @program: ZQDFS
 * @description: 文件系统
 * @author: zhouqi1
 * @create: 2018-12-10 21:46
 **/
public interface IFileSystem {

    /**
     * 根目录
     * @return
     */
    IDirectory root();

    /**
     * 返回指定路径的文件或目录
     * @param path
     * @return
     */
    INode find(String path);

    /**
     * 打开文件
     * @param filePath
     * @return
     */
    IFileInputStream open(String filePath) throws IOException;

    /**
     * 向文件中追加
     * @param filePath
     * @return
     */
    IFileOutputStream append(String filePath) throws IOException;

    /**
     * 创建新文件
     * @param path
     * @return
     */
    IFile createFile(String path) throws IOException;

    /**
     * 创建目录
     * @param path
     * @return
     */
    IDirectory mkdirs(String path) throws IOException;

    /**
     * 是否存在此路径文件
     * @param path
     * @return
     */
    boolean exists(String path);

    /**
     * 删除文件
     * @param path
     */
    void delete(String path);

    /**
     * 文件/目录改名
     * @param srcPath
     * @param targetPath
     */
    void rename(String srcPath, String targetPath) throws IOException;

    /**
     * 关闭
     */
    void close();

}
