package com.zq.dfs;

public interface IOutputStream {
    int write(byte[] data);

    int write(int offset, byte[] data);

    void close();
}
