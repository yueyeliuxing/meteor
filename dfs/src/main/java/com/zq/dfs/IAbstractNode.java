package com.zq.dfs;

import com.zq.dfs.constants.IFileSystemConstants;

import java.util.Date;

/**
 * @program: ZQDFS
 * @description: 节点
 * @author: zhouqi1
 * @create: 2018-12-10 20:11
 **/
public abstract class IAbstractNode implements INode {

    /**
     * 父级
     */
    protected INode parent;

    /**
     * 名称
     */
    protected String name;

    /**
     * 路径
     */
    protected String path;

    /**
     * 创建时间
     */
    protected Date createTime;

    /**
     * 最后修改时间
     */
    protected Date lastModifyTime;

    public String extractName(String path) {
        if(path.equals(IFileSystemConstants.PATH_SEPARATOR)){
            return "";
        }
        String[] temp = path.split(IFileSystemConstants.PATH_SEPARATOR);
        return temp[temp.length-1];
    }

    /**
     * 父级
     * @return
     */
    @Override
    public void parent(INode parent){
        this.parent = parent;
    }

    /**
     * 父级
     * @return
     */
    @Override
    public INode parent(){
        return parent;
    }

    /**
     * 名称
     * @return
     */
    @Override
    public void name(String name){
        this.name = name;
    }

    /**
     * 名称
     * @return
     */
    @Override
    public String name(){
        return name;
    }

    /**
     *  路径
     * @return
     */
    @Override
    public String path(){
        return path;
    }

    @Override
    public void path(String path) {
        this.path = path;
    }

    @Override
    public Date createTime() {
        return createTime;
    }

    @Override
    public Date lastModifyTime() {
        return lastModifyTime;
    }

    @Override
    public void distory() {

    }

    @Override
    public void close() {

    }

    @Override
    public byte[] serialize() {
        return new byte[0];
    }

    @Override
    public void deserialize(byte[] data) {

    }
}
