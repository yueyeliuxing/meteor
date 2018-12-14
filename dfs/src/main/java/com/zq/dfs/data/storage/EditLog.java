package com.zq.dfs.data.storage;

import com.zq.dfs.Persistencer;

import java.nio.ByteBuffer;

/**
 * @program: ZQDFS
 * @description: 更改日志
 * @author: zhouqi1
 * @create: 2018-12-12 20:42
 **/
public class EditLog implements Persistencer {

    public enum EditLogType{
        EDIT_FILE((byte)1),
        EDIT_DIRECTORY((byte)2),
        DEL_PATH((byte)3),
        ;

        private byte value;

        EditLogType(byte value) {
            this.value = value;
        }

        public byte value() {
            return value;
        }

        public static EditLogType toEnum(byte value){
            for(EditLogType type : values()){
                if(type.value() == value){
                    return type;
                }
            }
            return null;
        }
    }

    /**
     * 日志更改类型
     */
    private byte type;

    /**
     * 日志内容
     */
    private byte[] value;

    public EditLog() {
    }

    public EditLog(byte type, byte[] value) {
        this.type = type;
        this.value = value;
    }

    public byte getType() {
        return type;
    }

    public byte[] getValue() {
        return value;
    }

    @Override
    public byte[] serialize() {
        int valueSize = 0;
        if(value != null){
            valueSize = value.length;
        }
        int capacity = 1 + 4 + valueSize;
        ByteBuffer byteBuffer = ByteBuffer.allocate(capacity);
        byteBuffer.put(type);
        byteBuffer.putInt(valueSize);
        if(valueSize > 0){
            byteBuffer.put(value);
        }
        return byteBuffer.array();
    }

    @Override
    public void deserialize(byte[] data) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        type = byteBuffer.get();
        int valueSize = byteBuffer.getInt();
        if(valueSize > 0){
            byte[] value = new byte[valueSize];
            byteBuffer.get(value);
            this.value = value;
        }
    }
}
