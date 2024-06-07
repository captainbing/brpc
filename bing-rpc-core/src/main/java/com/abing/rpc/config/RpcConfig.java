package com.abing.rpc.config;

import com.abing.rpc.serializer.SerializerKeys;
import lombok.Data;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 11:02
 * @Description RPC框架配置
 */
@Data
public class RpcConfig {

    /**
     * 名称
     */
    private String name = "bing-rpc";

    /**
     * 版本号
     */
    private String version = "1.0";

    /**
     * 主机名
     */
    private String host = "127.0.0.1";

    /**
     * 主机端口
     */
    private Integer port = 8080;

    /**
     * 是否使用mock数据
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = SerializerKeys.JDK;

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();


}
