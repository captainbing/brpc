package com.abing.rpc.server.tcp;


import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 17:42
 * @Description
 */
public class VertxTcpClientTest {

    @Test
    public void testTcpClient() throws InterruptedException {


        Vertx vertx = Vertx.vertx();
        NetClient tcpClient = vertx.createNetClient();

        tcpClient.connect(9999,"localhost",result->{

            NetSocket socket = result.result();
            if (result.succeeded()){

                String message = "Hello Server!Hello Server!Hello Server!Hello Server!";
                for (int i = 0; i < 1000; i++) {

                    socket.write(message);
                }

                socket.handler(buffer->{
                    System.out.println("buffer = " + buffer.toString());
                });

            }

        });
        TimeUnit.SECONDS.sleep(1000);



    }

    @Test
    public void testTcpServer() throws InterruptedException {


        Vertx vertx = Vertx.vertx();
        NetServer tcpServer = vertx.createNetServer();

        tcpServer.connectHandler(socket->{

            socket.handler(buffer->{

                String message = "Hello Server!Hello Server!Hello Server!Hello Server!";

                int messageLen = message.getBytes().length;

                if (buffer.getBytes().length < messageLen){
                    System.out.println("半包 messageLen = " + buffer.getBytes().length);
                    return;
                }
                if (buffer.getBytes().length > messageLen){
                    System.out.println("粘包 messageLen = " + buffer.getBytes().length);
                    return;
                }

                String str = new String(buffer.getBytes(), 0, messageLen);
                if (str.equals(message)){
                    System.out.println("good " + message + "消息长度: " + messageLen);
                }

            });

        });

        tcpServer.listen(9999,result->{
            if (result.succeeded()) {
                System.out.println("success");
            }else {
                System.out.println("fail");
            }
        });
        TimeUnit.SECONDS.sleep(1000);
    }


}