package com.abing.rpc.serializer;


import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 14:53
 * @Description JSON序列化
 */
public class JsonSerializer implements Serializer{


    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public <T> byte[] serialize(T t) throws IOException {

        return OBJECT_MAPPER.writeValueAsBytes(t);
    }

    @Override
    public <T> T deserialize(byte[] bytes, Class<T> type) throws IOException {

        T obj = OBJECT_MAPPER.readValue(bytes, type);
        if (obj instanceof RpcRequest){
            return handleRequest((RpcRequest) obj,type);
        }
        if (obj instanceof RpcResponse){
            return handleResponse((RpcResponse) obj,type);
        }
        return obj;
    }

    /**
     * Object原始对象类型会被擦除，做特殊处理
     * @param rpcRequest
     * @param type
     * @return
     * @param <T>
     */
    private <T> T handleRequest(RpcRequest rpcRequest, Class<T> type) throws IOException {

        Object[] param = rpcRequest.getParam();
        Class<?>[] paramTypes = rpcRequest.getParamTypes();

        for (int i = 0; i < paramTypes.length; i++) {

            Class<?> clazz = paramTypes[i];
            // 如果类型不同重新分配
            if (clazz.isAssignableFrom(param[i].getClass())){
                byte[] paramByte = OBJECT_MAPPER.writeValueAsBytes(param[i]);
                param[i] = OBJECT_MAPPER.readValue(paramByte, clazz);
            }

        }
        return type.cast(rpcRequest);

    }

    /**
     * Object原始对象类型会被擦除，做特殊处理
     * @param rpcResponse
     * @param type
     * @return
     * @param <T>
     * @throws IOException
     */
    private <T> T handleResponse(RpcResponse rpcResponse, Class<T> type) throws IOException{

        byte[] dataBytes = OBJECT_MAPPER.writeValueAsBytes(rpcResponse.getData());
        rpcResponse.setData(OBJECT_MAPPER.readValue(dataBytes, rpcResponse.getDataTypes()));

        return type.cast(rpcResponse);
    }



}
