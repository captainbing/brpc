package com.abing.rpc.registry.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/6/7 15:02
 * @Description 注册中心键
 */
@AllArgsConstructor
@Getter
public enum RegistryKeys {


    ETCD("etcd"),
    ZOOKEEPER("zookeeper");

    private String registryKey;

}
