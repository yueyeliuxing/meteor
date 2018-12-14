package com.zq.dfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @program: ZQDFS
 * @description: 目录
 * @author: zhouqi1
 * @create: 2018-12-10 20:10
 **/
public interface IDirectory extends INode {

    /**
     * 添加目录
     * @param directory
     */
    void addDirectory(IDirectory directory);

    /**
     * 添加文件
     * @param file
     */
    void addFile(IFile file);

    /**
     * 删除指定目录
     * @param directory
     */
    void remove(IDirectory directory);

    /**
     * 删除指定目录
     * @param file
     */
    void remove(IFile file);


    /**
     * 是否存在指定目录
     * @param directory
     * @return
     */
    boolean exists(IDirectory directory);

    /**
     * 是否存在指定文件
     * @param file
     * @return
     */
    boolean exists(IFile file);

    /**
     * 子目录
     * @return
     */
    List<IDirectory> subdirectories();
    /**
     * 子目录
     * @return
     */
    List<IFile> subFiles();
}
