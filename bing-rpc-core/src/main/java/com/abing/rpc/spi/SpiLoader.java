package com.abing.rpc.spi;

import cn.hutool.core.io.resource.ResourceUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 15:42
 * @Description spi加载机制 根据配置文件加载序列化器
 */
@Slf4j
public class SpiLoader {

    /**
     * 存储已加载的类
     */
    private static Map<String,Map<String,Class<?>>> loaderMap = new ConcurrentHashMap<>();


    /**
     * 对象实例缓存
     */
    private static Map<String,Object> instanceCache = new ConcurrentHashMap<>();

    /**
     * 系统 SPI 目录
     */
    private static final String RPC_SYSTEM_SPI_DIR = "META-INF/rpc/system/";

    /**
     * 用户自定义 SPI 目录
     */
    private static final String RPC_CUSTOM_SPI_DIR = "META-INF/rpc/custom/";

    /**
     * 扫描路径
     */
    private static final String[] SPI_SCAN_DIRS = {RPC_SYSTEM_SPI_DIR, RPC_CUSTOM_SPI_DIR};


    /**
     * 获取某个接口的实例
     * @param clazz
     * @param key
     * @return
     * @param <T>
     */
    public static <T> T getInstance(Class<T> clazz,String key) {

        String className = clazz.getName();

        Map<String, Class<?>> keyClassMap = loaderMap.get(className);
        if (keyClassMap == null){
            throw new RuntimeException("未找到" + className + "对应的实现类");
        }
        if (!keyClassMap.containsKey(key)){
            throw new RuntimeException("未找到" + className + "对应的实现类");
        }

        Class<?> implClass = keyClassMap.get(key);
        String implClassName = implClass.getName();
        if (!instanceCache.containsKey(implClassName)){
            try {
                instanceCache.put(implClassName,implClass.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                String errorMsg = String.format("实例化类失败，类名：%s",implClassName);
                throw new RuntimeException(errorMsg,e);
            }
        }
        return (T) instanceCache.get(implClassName);
    }



    /**
     * 加载某个类型
     * @param loadClass
     * @return
     */
    public static Map<String,Class<?>> load(Class<?> loadClass){

        Map<String,Class<?>> keyClassMap = new HashMap<>();

        for (String scanDir : SPI_SCAN_DIRS) {
            List<URL> resources = ResourceUtil.getResources(scanDir + loadClass.getName());

            // 读取每个资源文件
            for (URL resource : resources) {
                try {
                    InputStreamReader inputStreamReader = new InputStreamReader(resource.openStream());

                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    String line;
                    while((line = bufferedReader.readLine()) != null){
                        String[] strArray = line.split("=");
                        if (strArray.length > 1){
                            String key = strArray[0];
                            String className = strArray[1];
                            keyClassMap.put(key,Class.forName(className));
                        }
                    }

                } catch (Exception e) {
                    log.error("spi resource load error");
                }
            }
        }
        loaderMap.put(loadClass.getName(),keyClassMap);
        return keyClassMap;
    }
}
