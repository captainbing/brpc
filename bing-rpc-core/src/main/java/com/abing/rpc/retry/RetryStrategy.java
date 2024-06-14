package com.abing.rpc.retry;

import com.abing.rpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 9:54
 * @Description 重试策略接口
 */
public interface RetryStrategy {


    /**
     * 执行重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception;



}
