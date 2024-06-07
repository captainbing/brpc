package com.abing.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.example.common.model.User;
import com.abing.example.common.service.UserService;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.serializer.impl.JdkSerializer;
import com.abing.rpc.serializer.Serializer;

import java.io.IOException;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 15:22
 * @Description 静态代理实现
 */
public class UserServiceProxy implements UserService {


    @Override
    public User getUser(User user) {

        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(UserService.class.getName())
                                          .methodName("getUser")
                                          .paramTypes(new Class[]{User.class})
                                          .args(new Object[]{user})
                                          .build();
        try {
            byte[] requestByte = serializer.serialize(rpcRequest);
            HttpResponse httpResponse = HttpRequest.post("http://localhost:8888")
                                                   .body(requestByte)
                                                   .timeout(5000)
                                                   .execute();
            byte[] result = httpResponse.bodyBytes();
            RpcResponse rpcResponse = serializer.deserialize(result, RpcResponse.class);
            return (User) rpcResponse.getData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
