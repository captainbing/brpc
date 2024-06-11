package com.abing.rpc.server.tcp;

import com.abing.rpc.server.Server;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 14:33
 * @Description TCP 服务器
 */
@Slf4j
public class VertxTcpServer implements Server {

    @Override
    public void doStart(int port) {

        Vertx vertx = Vertx.vertx();
        NetServer tcpServer = vertx.createNetServer();

        // 处理连接
        tcpServer.connectHandler(new VertxTcpHandler());

        tcpServer.listen(port, result -> {
            if (result.succeeded()) {
                log.info("TCP Server started on port {}", port);
            }else {
                log.info("TCP Server failed to start on port {}", port);
            }
        });

    }

}
