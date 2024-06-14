package com.abing.rpc.loadbalance;

import com.abing.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;

/**
 * @Author CaptainBing
 * @Date 2024/6/13 11:02
 * @Description 负载均衡选择器
 */
public interface LoadBalancer {


    /**
     * 选择服务调用
     * @param requestParam
     * @param serviceMetaInfoList
     * @return
     */
    ServiceMetaInfo select(Map<String,Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList);


}
