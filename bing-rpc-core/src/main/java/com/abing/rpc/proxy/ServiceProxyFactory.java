package com.abing.rpc.proxy;

import com.abing.rpc.BRpcApplication;

import java.lang.reflect.Proxy;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 16:14
 * @Description
 */
public class ServiceProxyFactory {


    /**
     * 根据服务类创建代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    public static <T> T getProxy(Class<T> serviceClass){
        if (BRpcApplication.getBRpcConfig().isMock()){
            return getMockServiceProxy(serviceClass);
        }
        return getRemoteServiceProxy(serviceClass);

    }

    /**
     * 获取远程服务代理对象
     * @param serviceClass
     * @return
     * @param <T>
     */
    private static <T> T getRemoteServiceProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                                          new Class[]{serviceClass},
                                          new ServiceProxy());
    }

    /**
     * 获取mockServiceProxy
     * @param serviceClass
     * @return
     * @param <T>
     */
    private static <T> T getMockServiceProxy(Class<T> serviceClass) {
        return (T) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                                          new Class[]{serviceClass},
                                          new MockServiceProxy());
    }


}
