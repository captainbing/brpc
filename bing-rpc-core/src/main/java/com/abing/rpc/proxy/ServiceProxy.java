package com.abing.rpc.proxy;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.rpc.BRpcApplication;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.serializer.JdkSerializer;
import com.abing.rpc.serializer.Serializer;
import com.abing.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 15:35
 * @Description
 */
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Serializer serializer = SerializerFactory.getInstance(BRpcApplication.getBRpcConfig().getSerializer());

        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(method.getDeclaringClass().getName())
                                          .methodName(method.getName())
                                          .paramTypes(method.getParameterTypes())
                                          .param(args)
                                          .build();
        try {
            byte[] requestByte = serializer.serialize(rpcRequest);
            // 发送请求
            // TODO 地址被硬编码，需要使用注册中心和服务发现机制解决
            HttpResponse httpResponse = HttpRequest.post("http://localhost:8888")
                                                   .body(requestByte)
                                                   .timeout(5000)
                                                   .execute();
            byte[] result = httpResponse.bodyBytes();
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
