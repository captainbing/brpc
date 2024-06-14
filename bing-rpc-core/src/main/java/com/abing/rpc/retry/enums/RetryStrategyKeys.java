package com.abing.rpc.retry.enums;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 10:12
 * @Description
 */
public enum RetryStrategyKeys {


    /**
     * 不采取重试策略
     */
    NO(),
    /**
     * 固定时间间隔重试策略
     */
    FIXED();

}
