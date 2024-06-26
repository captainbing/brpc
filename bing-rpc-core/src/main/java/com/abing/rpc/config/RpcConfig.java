package com.abing.rpc.config;

import com.abing.rpc.protocol.ProtocolMessageSerializerEnum;
import com.abing.rpc.retry.enums.RetryStrategyKeys;
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
    private String name = "brpc";

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
    private Integer port = 20003;

    /**
     * 是否使用mock数据
     */
    private boolean mock = false;

    /**
     * 序列化器
     */
    private String serializer = ProtocolMessageSerializerEnum.JDK.name();

    /**
     * 注册中心配置
     */
    private RegistryConfig registryConfig = new RegistryConfig();

    /**
     * 重试策略
     */
    private String retryStrategy = RetryStrategyKeys.NO.name();


}
