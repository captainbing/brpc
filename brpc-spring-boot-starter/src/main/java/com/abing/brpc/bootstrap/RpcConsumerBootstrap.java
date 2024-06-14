package com.abing.brpc.bootstrap;

import com.abing.brpc.annotation.BRpcReference;
import com.abing.rpc.proxy.ServiceProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 13:13
 * @Description RPC 服务消费者启动
 */
public class RpcConsumerBootstrap implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {


        Class<?> beanClass = bean.getClass();
        Field[] declaredFields = beanClass.getDeclaredFields();

        for (Field field : declaredFields) {

            BRpcReference bRpcReference = field.getAnnotation(BRpcReference.class);
            if (bRpcReference != null) {
                // 为属性生成代理对象
                Class<?> interfaceClass = bRpcReference.interfaceClass();
                if (interfaceClass == void.class){
                    interfaceClass = field.getType();
                }
                field.setAccessible(true);
                Object proxyObject = ServiceProxyFactory.getProxy(interfaceClass);
                try {
                    field.set(bean, proxyObject);
                    field.setAccessible(false);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("为字段注入代理对象失败" + e);
                }
            }

        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
