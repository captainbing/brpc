package com.abing.rpc.retry.impl;

import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.retry.RetryStrategy;

import java.util.concurrent.Callable;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 9:57
 * @Description 不使用重试机制
 */
public class NoRetryStrategy implements RetryStrategy {

    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
