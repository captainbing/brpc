package com.abing.rpc.retry;

import com.abing.rpc.spi.SpiLoader;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 10:14
 * @Description
 */
public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }


    /**
     * 获取重试策略
     * @param key
     * @return
     */
    public static RetryStrategy getInstance(String key){
        return SpiLoader.getInstance(RetryStrategy.class, key);
    }

}
