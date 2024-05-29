package com.abing.rpc.spi;

import com.abing.rpc.serializer.Serializer;

import java.util.ServiceLoader;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 16:18
 * @Description JDK自带spi实现机制，自动扫描resource目录 ==> META-INF/services
 */
public class SystemSpiLoader {


    /**
     * 加载spi序列化类
     * @return
     */
    public static Serializer load(){
        Serializer serializer = null;
        ServiceLoader<Serializer> serializerServiceLoader = ServiceLoader.load(Serializer.class);
        for (Serializer service : serializerServiceLoader) {
            serializer = service;
        }
        return serializer;
    }

    public static void main(String[] args) {
        load();
    }


}
