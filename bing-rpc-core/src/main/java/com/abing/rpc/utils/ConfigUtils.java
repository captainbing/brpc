package com.abing.rpc.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.dialect.Props;

/**
 * @Author CaptainBing
 * @Date 2024/5/29 11:07
 * @Description
 */
public class ConfigUtils {


    /**
     * 加载配置
     * @param clazz
     * @param prefix
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix){
        return loadConfig(clazz,prefix,"rpc");
    }


    /**
     * 加载配置环境
     * @param clazz
     * @param prefix
     * @param environment
     * @return
     * @param <T>
     */
    public static <T> T loadConfig(Class<T> clazz, String prefix,String environment){

        StringBuilder configFileBuilder = new StringBuilder("application");

        if (StrUtil.isNotBlank(environment)){
            configFileBuilder.append("-").append(environment);
        }
        configFileBuilder.append(".properties");
        Props props = new Props(configFileBuilder.toString());
        return props.toBean(clazz,prefix);

    }


}
