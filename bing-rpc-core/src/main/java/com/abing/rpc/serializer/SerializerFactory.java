package com.abing.rpc.serializer;



import com.abing.rpc.serializer.impl.JdkSerializer;
import com.abing.rpc.spi.SpiLoader;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 15:18
 * @Description
 */
public class SerializerFactory {

    static {
        SpiLoader.load(Serializer.class);
    }


    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();


    /**
     * 获取实例
     * @param key
     * @return
     */
    public static Serializer getInstance(String key){

        return SpiLoader.getInstance(Serializer.class,key);

    }



}
