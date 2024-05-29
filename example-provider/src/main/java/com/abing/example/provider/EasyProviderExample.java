package com.abing.example.provider;

import com.abing.example.common.service.UserService;
import com.abing.rpc.registry.LocalRegistry;
import com.abing.rpc.server.HttpServer;
import com.abing.rpc.server.VertxHttpServer;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:20
 * @Description
 */
public class EasyProviderExample {


    public static void main(String[] args) {

        LocalRegistry.register(UserService.class.getName(),UserServiceImpl.class);

        HttpServer httpServer = new VertxHttpServer();

        httpServer.doStart(8888);

    }


}
