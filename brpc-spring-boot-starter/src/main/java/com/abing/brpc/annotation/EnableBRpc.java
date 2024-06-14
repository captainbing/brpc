package com.abing.brpc.annotation;

import com.abing.brpc.bootstrap.RpcConsumerBootstrap;
import com.abing.brpc.bootstrap.RpcInitBootstrap;
import com.abing.brpc.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 11:00
 * @Description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class, RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableBRpc {

    /**
     * 是否启动server
     * @return
     */
    boolean needServer() default true;

}
