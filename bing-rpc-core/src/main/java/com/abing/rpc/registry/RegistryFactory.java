package com.abing.rpc.registry;

import com.abing.rpc.registry.impl.EtcdRegistry;
import com.abing.rpc.spi.SpiLoader;

/**
 * @Author CaptainBing
 * @Date 2024/6/7 14:52
 * @Description
 */
public class RegistryFactory {


    static {
        SpiLoader.load(Registry.class);
    }

    /**
     * 默认注册中心
     */
    private static final Registry DEFAULT_REGISTRY = new EtcdRegistry();

    /**
     * 获取实例
     *
     * @param key
     * @return
     */
    public static Registry getInstance(String key) {
        return SpiLoader.getInstance(Registry.class, key);
    }



}
