package com.abing.brpc.annotation;

import com.abing.rpc.constant.RpcConstant;
import com.abing.rpc.loadbalance.enums.LoadBalancerKeys;
import com.abing.rpc.retry.enums.RetryStrategyKeys;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 11:00
 * @Description
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BRpcReference {


    /**
     *
     * @return 服务接口类
     */
    Class<?> interfaceClass() default void.class;

    /**
     *
     * @return 服务版本号
     */
    String serviceVersion() default RpcConstant.DEFAULT_SERVICE_VERSION;

    /**
     *
     * @return 负载均衡器
     */
    LoadBalancerKeys loadBalance() default LoadBalancerKeys.HASH;

    /**
     *
     * @return 重试策略
     */
    RetryStrategyKeys retryStrategy() default RetryStrategyKeys.NO;

    /**
     *
     * @return 模拟调用
     */
    boolean mock() default false;


}
