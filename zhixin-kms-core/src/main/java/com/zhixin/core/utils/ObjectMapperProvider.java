package com.zhixin.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObjectMapperProvider{
    final ObjectMapper defaultObjectMapper;

    public ObjectMapperProvider() {
        defaultObjectMapper = createDefaultMapper();
    }

    public ObjectMapper getInstance(){
    	return defaultObjectMapper;
    }


    private static ObjectMapper createDefaultMapper() {
        final ObjectMapper result = new ObjectMapper();
        //序列化JSON 字符串时忽略属性为NULL 的字段
        result.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //反序列化成JAVA 对象时 忽略不存在的属性或字段
        SimpleModule long2StringModule = new SimpleModule("long2StringModule", Version.unknownVersion());
        long2StringModule.addSerializer(new Long2StringSerializer()); 
        result.registerModule(long2StringModule);
        return result;
    }
}
