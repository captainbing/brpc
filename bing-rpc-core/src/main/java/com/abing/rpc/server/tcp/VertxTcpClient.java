package com.abing.rpc.server.tcp;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 15:03
 * @Description
 */
public class VertxTcpClient {


    private void start(int port){

        Vertx vertx = Vertx.vertx();
        NetClient tcpClient = vertx.createNetClient();

        tcpClient.connect(port, "localhost", result -> {
            if (result.succeeded()) {
                NetSocket socket = result.result();
                socket.write("Hello Server");
                socket.handler(buffer -> {

                    System.out.println("Server response: " + buffer.toString());
                });
            }else {
                System.out.println("Connect failed");
            }
        });
    }


    public static void main(String[] args) {

        new VertxTcpClient().start(8888);
    }


}
