package com.abing.example.provider;

import com.abing.example.common.service.UserService;
import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RegistryConfig;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.registry.LocalRegistry;
import com.abing.rpc.registry.Registry;
import com.abing.rpc.registry.RegistryFactory;
import com.abing.rpc.server.Server;
import com.abing.rpc.server.http.VertxHttpServer;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:20
 * @Description
 */
public class EasyProviderExample {


    public static void main(String[] args) {

        // RPC 框架初始化
        RpcApplication.init();

        // 注册服务
        String serviceName = UserService.class.getName();
        LocalRegistry.register(serviceName, UserServiceImpl.class);

        // 注册服务到注册中心
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceHost(rpcConfig.getHost());
        serviceMetaInfo.setServicePort(rpcConfig.getPort());
        try {
            registry.register(serviceMetaInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        Server httpServer = new VertxHttpServer();

        httpServer.doStart(rpcConfig.getPort());

    }


}
