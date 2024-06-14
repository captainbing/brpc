package com.abing.example.provider;

import com.abing.example.common.service.UserService;
import com.abing.rpc.bootstrap.ProviderBootstrap;
import com.abing.rpc.model.ServiceRegisterInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:20
 * @Description
 */
public class EasyProviderExample {


    public static void main(String[] args) {


        ServiceRegisterInfo<UserService> serviceRegisterInfo = new ServiceRegisterInfo(UserService.class.getName(), UserServiceImpl.class);

        List<ServiceRegisterInfo<?>> serviceRegisterInfos = new ArrayList<>();
        serviceRegisterInfos.add(serviceRegisterInfo);

        ProviderBootstrap.init(serviceRegisterInfos);


    }


}
