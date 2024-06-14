package com.abing.rpc.retry.impl;

import com.abing.rpc.retry.RetryStrategy;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 10:08
 * @Description
 */
public class FixedIntervalRetryStrategyTest {


    RetryStrategy retryStrategy = new FixedIntervalRetryStrategy();

    @Test
    public void doRetry() {

        try {
            retryStrategy.doRetry(()->{
                System.out.println("重试");
                throw new RuntimeException();
            });
        } catch (Exception e) {
            System.out.println("重试多次失败");
            throw new RuntimeException(e);
        }

    }
}