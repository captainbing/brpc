package com.abing.rpc.model;

import lombok.Data;

/**
 * @Author CaptainBing
 * @Date 2024/6/7 13:54
 * @Description 服务注册信息
 */
@Data
public class ServiceMetaInfo {


    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本号
     */
    private String serviceVersion = "1.0";
    /**
     * 服务域名
     */
    private String serviceHost;
    /**
     * 服务端口
     */
    private Integer servicePort;
    /**
     * 服务分组 TODO 暂未实现
     */
    private String serviceGroup;


    /**
     * 获取服务名称
     * @return
     */
    public String getServiceKey(){
        return String.format("%s:%s",serviceName,serviceVersion);
    }

    /**
     * 获取服务注册键名
     * @return
     */
    public String getServiceNodeKey(){
        return String.format("%s/%s:%s", getServiceKey(), serviceHost, servicePort);
    }

    /**
     * 获取完整服务地址
     *
     * @return
     */
    public String getServiceAddress() {
            if (serviceHost.contains("http")){
            return String.format("http://%s:%s", serviceHost, servicePort);
        }
        return String.format("%s:%s", serviceHost, servicePort);
    }

}
