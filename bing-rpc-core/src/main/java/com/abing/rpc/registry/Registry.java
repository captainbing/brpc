package com.abing.rpc.registry;

import com.abing.rpc.config.RegistryConfig;
import com.abing.rpc.model.ServiceMetaInfo;

import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/6/7 14:08
 * @Description 注册中心接口
 */
public interface Registry {

    /**
     * 初始化注册中心
     * @param registryConfig
     */
    void init(RegistryConfig registryConfig);

    /**
     * 注册服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void register(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 注销服务
     * @param serviceMetaInfo
     * @throws Exception
     */
    void unRegister(ServiceMetaInfo serviceMetaInfo) throws Exception;

    /**
     * 服务发现
     * @param serviceKey
     * @return
     */
    List<ServiceMetaInfo> serviceDiscovery(String serviceKey);

    /**
     * 注册中心销毁，用于项目关闭后释放资源
     */
    void destroy();

    /**
     * 心跳机制
     */
    void heartBeat();


}
