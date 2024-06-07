package com.abing.rpc.server.http;

import com.abing.rpc.server.Server;
import io.vertx.core.Vertx;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 14:27
 * @Description
 */
public class VertxHttpServer implements Server {


    @Override
    public void doStart(int port) {

        Vertx vertx = Vertx.vertx();

        io.vertx.core.http.HttpServer vertxHttpServer = vertx.createHttpServer();

        vertxHttpServer.requestHandler(new HttpServerHandler());

        // 启动 Http Server 并监听端口
        vertxHttpServer.listen(port,result->{
            if (result.succeeded()){
                System.out.println("Vertx Server is listening on port " + port);
            }else {
                System.out.println("Fail to start Vertx Server " + result.cause());
            }
        });


    }
}
