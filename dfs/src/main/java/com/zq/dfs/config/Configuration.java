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

    public Configuration(Map<String, String> config) {
        this.config = new HashMap<>();
        this.config.putAll(config);
    }

    public String getParam(String key){
        return config.get(key);
    }
}
