package com.abing.rpc.serializer.impl;

import com.abing.rpc.serializer.Serializer;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 15:21
 * @Description
 */
public class KryoSerializer implements Serializer {

    @Override
    public <T> byte[] serialize(T t) throws IOException {
        return new byte[0];
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {
        return null;
    }
}
