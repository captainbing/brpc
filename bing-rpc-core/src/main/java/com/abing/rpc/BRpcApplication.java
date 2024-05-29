package com.abing.rpc;

import com.abing.rpc.config.BRpcConfig;
import com.abing.rpc.constant.BRpcConstant;
import com.abing.rpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 13:40
 * @Description
 */
@Slf4j
public class BRpcApplication {

    private static volatile BRpcConfig bRpcConfig;
    
    
    public static void init(BRpcConfig newBRpcConfig){
        bRpcConfig = newBRpcConfig;
        log.info("bRpcConfig init success {}",bRpcConfig);
    }

    /**
     * 初始化Rpc全局配置
     */
    public static void init(){
        BRpcConfig newRpcConfig = null;
        try{
            newRpcConfig = ConfigUtils.loadConfig(BRpcConfig.class, BRpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newRpcConfig = new BRpcConfig();
        }
        init(newRpcConfig);
    }


    /**
     * 获取bRpcConfig 双检锁实现
     * @return
     */
    public static BRpcConfig getBRpcConfig(){
        if(bRpcConfig == null){
            synchronized (BRpcApplication.class){
                if(bRpcConfig == null){
                    init();
                }
            }
        }
        return bRpcConfig;
    }

    
    
}
