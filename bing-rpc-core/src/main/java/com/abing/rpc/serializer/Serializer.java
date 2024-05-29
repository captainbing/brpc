package com.abing.rpc.serializer;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:54
 * @Description
 */
public interface Serializer {


    /**
     * 序列化
     * @param t
     * @return
     * @param <T>
     */
    <T> byte[] serialize(T t) throws IOException;


    /**
     * 反序列化
     * @param bytes
     * @param type
     * @return
     * @param <T>
     */
    <T> T deserialize(byte[] bytes,Class<T> type) throws IOException;


}
