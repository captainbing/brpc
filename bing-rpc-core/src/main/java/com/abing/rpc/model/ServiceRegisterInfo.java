package com.abing.rpc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author CaptainBing
 * @Date 2024/6/14 10:42
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceRegisterInfo<T> {


    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 实现类
     */
    private Class<? extends T> implClass;



}
