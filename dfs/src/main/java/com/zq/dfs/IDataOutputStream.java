package com.zq.dfs;

public interface IDataOutputStream {
    int write(byte[] data);

    int write(int offset, byte[] data);

    void close();
}
