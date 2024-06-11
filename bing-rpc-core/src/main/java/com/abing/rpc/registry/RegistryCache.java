package com.abing.rpc.registry;

import com.abing.rpc.model.ServiceMetaInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 10:45
 * @Description 注册信息缓存
 */
public class RegistryCache {


    private List<ServiceMetaInfo> serviceMetaInfoCache = new ArrayList<>();


    /**
     * 写缓存
     * @param serviceMetaInfoList
     */
    public void writeCache(List<ServiceMetaInfo> serviceMetaInfoList){
        this.serviceMetaInfoCache = serviceMetaInfoList;
    }

    /**
     * 读缓存
     * @return
     */
    public List<ServiceMetaInfo> readCache(){
        return serviceMetaInfoCache;
    }

    /**
     * 清空缓存
     */
    public void clearCache(){
        this.serviceMetaInfoCache = new ArrayList<>();
    }


}
