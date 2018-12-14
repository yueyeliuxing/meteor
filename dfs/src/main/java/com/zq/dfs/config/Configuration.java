package com.zq.dfs.config;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ZQDFS
 * @description: 配置类
 * @author: zhouqi1
 * @create: 2018-12-12 11:31
 **/
public class Configuration {

    private Map<String, String> config;

    public Configuration() {
        this.config = new HashMap<>();
    }

    public void put(String key, String value){
        config.put(key, value);
    }

    public String get(String key){
        return config.get(key);
    }
}
