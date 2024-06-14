package com.abing.rpc.loadbalance;

import com.abing.rpc.model.ServiceMetaInfo;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author CaptainBing
 * @Date 2024/6/13 11:05
 * @Description 一致性hash算法
 */
public class ConsistentHashLoadBalancer implements LoadBalancer{


    /**
     * 一致性hash环
     */
    private final TreeMap<Integer,ServiceMetaInfo> virtualNodes = new TreeMap<>();


    /**
     * 虚拟节点数
     */
    private static final int VIRTUAL_NODE_NUM = 100;


    @Override
    public ServiceMetaInfo select(Map<String, Object> requestParam, List<ServiceMetaInfo> serviceMetaInfoList) {

        if (serviceMetaInfoList.isEmpty()){
            return null;
        }

        // 构建虚拟环
        for (ServiceMetaInfo serviceMetaInfo : serviceMetaInfoList) {
            for (int i = 0; i < VIRTUAL_NODE_NUM; i++) {
                int hash = getHash(serviceMetaInfo.getServiceAddress() + "#" + i);
                virtualNodes.put(hash,serviceMetaInfo);
            }
        }
        int hash = getHash(requestParam);
        Map.Entry<Integer, ServiceMetaInfo> serviceMetaInfoEntry = virtualNodes.ceilingEntry(hash);
        if (serviceMetaInfoEntry == null){
            serviceMetaInfoEntry = virtualNodes.firstEntry();
        }
        return serviceMetaInfoEntry.getValue();
    }

    /**
     * 获取hash值
     * @param key
     * @return
     */
    private int getHash(Object key){
        return key.hashCode();
    }
}
