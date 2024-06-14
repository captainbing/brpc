package com.abing.brpc.bootstrap;

import com.abing.brpc.annotation.EnableBRpc;
import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.server.tcp.VertxTcpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 11:26
 * @Description
 */
@Slf4j
public class RpcInitBootstrap implements ImportBeanDefinitionRegistrar {

    /**
     * spring boot初始化时执行
     * @param importingClassMetadata annotation metadata of the importing class
     * @param registry current bean definition registry
     */
    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {


        boolean needServer = (boolean) importingClassMetadata.getAnnotationAttributes(EnableBRpc.class.getName()).get("needServer");

        // RPC框架初始化
        RpcApplication.init();

        // 全局配置
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        if (needServer){
            new VertxTcpServer().doStart(rpcConfig.getPort());
            return;
        }

        log.info("brpc tcp server not start");

    }
}
