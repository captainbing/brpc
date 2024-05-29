package com.abing.rpc.registry;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:43
 * @Description
 */
public class LocalRegistry {


    private static final Map<String,Class<?>> map = new ConcurrentHashMap<>();

    /**
     * 注册
     * @param serviceName
     * @param clazz
     */
    public static void register(String serviceName,Class<?> clazz){
        map.put(serviceName,clazz);
    }

    /**
     * 获取
     * @param serviceName
     * @return
     */
    public static Class<?> get(String serviceName){
        return map.get(serviceName);
    }


    /**
     * 删除
     * @param serviceName
     * @param clazz
     */
    public static void remove(String serviceName,Class<?> clazz){
        map.remove(serviceName);
    }



}
