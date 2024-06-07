package com.abing.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.constant.RpcConstant;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.registry.Registry;
import com.abing.rpc.registry.RegistryFactory;
import com.abing.rpc.serializer.Serializer;
import com.abing.rpc.serializer.SerializerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 15:35
 * @Description
 */
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());
        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(serviceName)
                                          .methodName(method.getName())
                                          .paramTypes(method.getParameterTypes())
                                          .args(args)
                                          .build();
        try {
            // 序列化
            byte[] requestByte = serializer.serialize(rpcRequest);


            // 从注册中心获取服务提供者请求地址
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
            List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
            if (CollUtil.isEmpty(serviceMetaInfoList)) {
                throw new RuntimeException("暂无服务地址");
            }
            ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);


            // 发送请求
            // TODO 地址被硬编码，需要使用注册中心和服务发现机制解决
            HttpResponse httpResponse = HttpRequest.post(selectedServiceMetaInfo.getServiceAddress())
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
