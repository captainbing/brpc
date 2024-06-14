package com.abing.brpc.bootstrap;

import com.abing.brpc.annotation.BRpcService;
import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RegistryConfig;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.registry.LocalRegistry;
import com.abing.rpc.registry.Registry;
import com.abing.rpc.registry.RegistryFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 11:40
 * @Description
 */
public class RpcProviderBootstrap implements BeanPostProcessor {


    /**
     * 在每一个bean初始化后执行
     * @param bean the new bean instance
     * @param beanName the name of the bean
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        Class<?> beanClass = bean.getClass();
        BRpcService bRpcService = beanClass.getAnnotation(BRpcService.class);

        if (bRpcService != null){

            // 获取服务基本信息
            Class<?> interfaceClass = bRpcService.interfaceClass();
            if (interfaceClass==void.class) {
                interfaceClass = beanClass.getInterfaces()[0];
            }
            String serviceName = interfaceClass.getName();
            String serviceVersion = bRpcService.serviceVersion();

            // 注册服务
            LocalRegistry.register(serviceName,beanClass);
            RpcConfig rpcConfig = RpcApplication.getRpcConfig();
            RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
            Registry registry = RegistryFactory.getInstance(registryConfig.getRegistry());

            ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
            serviceMetaInfo.setServiceName(serviceName);
            serviceMetaInfo.setServiceVersion(serviceVersion);
            serviceMetaInfo.setServiceHost(rpcConfig.getHost());
            serviceMetaInfo.setServicePort(rpcConfig.getPort());

            try {
                registry.register(serviceMetaInfo);
            } catch (Exception e) {
                throw new RuntimeException("服务注册失败: " + "服务名称: " + serviceName + " 注册中心: " + registryConfig.getRegistry() + e);
            }

        }

        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
