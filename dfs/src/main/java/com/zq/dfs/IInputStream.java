package com.zq.dfs;

public interface IInputStream {
    byte read();

    void read(byte[] data);

    void read(byte[] data, int offset, int len);

    int available();

    void close();
}
