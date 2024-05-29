package com.abing.rpc.proxy;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 14:13
 * @Description
 */
@Slf4j
public class MockServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // 获取方法返回值类型
        Class<?> methodReturnType = method.getReturnType();
        log.info("mock methodReturnType:{}",methodReturnType);

        return getMockObject(methodReturnType);
    }

    /**
     * 生成指定类型的默认数据
     * @param returnType
     * @return
     */
    private Object getMockObject(Class<?> returnType) {

        // 返回类型是否是基本类型
        if (returnType.isPrimitive()){
            if (returnType == boolean.class){
                return false;
            }
            if (returnType == short.class){
                return 0;
            }
            if (returnType == int.class){
                return 0;
            }
            if (returnType == long.class){
                return 0;
            }
        }
        return null;
    }

}
