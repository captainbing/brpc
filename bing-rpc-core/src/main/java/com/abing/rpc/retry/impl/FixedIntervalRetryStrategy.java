package com.abing.rpc.retry.impl;

import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.retry.RetryStrategy;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 10:00
 * @Description 固定事件间隔重试策略
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy {


    @Override
    public RpcResponse doRetry(Callable<RpcResponse> callable) throws Exception {

        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                                              .retryIfExceptionOfType(Exception.class)
                                              .withWaitStrategy(WaitStrategies.fixedWait(3, TimeUnit.SECONDS))
                                              .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                                              .withRetryListener(new RetryListener() {
                                                  @Override
                                                  public <V> void onRetry(Attempt<V> attempt) {
                                                      log.info("第" + attempt.getAttemptNumber() + "次重试");
                                                  }
                                              }).build();


        return retryer.call(callable);
    }
}
