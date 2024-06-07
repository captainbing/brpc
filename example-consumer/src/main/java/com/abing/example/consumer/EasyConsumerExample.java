package com.abing.example.consumer;

import com.abing.example.common.model.User;
import com.abing.example.common.service.UserService;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.proxy.ServiceProxyFactory;
import com.abing.rpc.utils.ConfigUtils;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:21
 * @Description
 */
public class EasyConsumerExample {


    public static void main(String[] args) {

//        UserServiceProxy userServiceProxy = new UserServiceProxy();
        RpcConfig rpcConfig = ConfigUtils.loadConfig(RpcConfig.class, "brpc");
        System.out.println("bRpcConfig = " + rpcConfig);
        UserService userServiceProxy = ServiceProxyFactory.getProxy(UserService.class);
        User user = new User();
        user.setUserName("熊炳忠");
        User newUser = userServiceProxy.getUser(user);
        System.out.println("newUser = " + newUser);

        int number = userServiceProxy.getNumber();
        System.out.println("number = " + number);

    }


}
