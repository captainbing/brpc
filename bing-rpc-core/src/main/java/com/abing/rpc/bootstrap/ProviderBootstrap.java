package com.abing.rpc.bootstrap;

import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RegistryConfig;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.model.ServiceRegisterInfo;
import com.abing.rpc.registry.LocalRegistry;
import com.abing.rpc.registry.Registry;
import com.abing.rpc.registry.RegistryFactory;
import com.abing.rpc.server.tcp.VertxTcpServer;

import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 10:41
 * @Description
 */
public class ProviderBootstrap {


    public static void init(List<ServiceRegisterInfo<?>> serviceRegisterInfoList){

        // RPC框架初始化(全局配置,注册中心)
        RpcApplication.init();

        RpcConfig rpcConfig = RpcApplication.getRpcConfig();

        for (ServiceRegisterInfo serviceRegisterInfo : serviceRegisterInfoList) {

            // 注册服务
            String serviceName = serviceRegisterInfo.getServiceName();
            LocalRegistry.register(serviceName, serviceRegisterInfo.getImplClass());

            // 注册服务到注册中心
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
        }

        VertxTcpServer vertxTcpServer = new VertxTcpServer();
        vertxTcpServer.doStart(rpcConfig.getPort());
    }


}
