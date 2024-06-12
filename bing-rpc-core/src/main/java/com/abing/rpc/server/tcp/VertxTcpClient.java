package com.abing.rpc.server.tcp;

import cn.hutool.core.util.IdUtil;
import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.protocol.ProtocolConstant;
import com.abing.rpc.protocol.ProtocolMessage;
import com.abing.rpc.protocol.ProtocolMessageSerializerEnum;
import com.abing.rpc.protocol.ProtocolMessageTypeEnum;
import com.abing.rpc.server.codec.ProtocolMessageDecoder;
import com.abing.rpc.server.codec.ProtocolMessageEncoder;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 15:03
 * @Description
 */
@Slf4j
public class VertxTcpClient {

    public static RpcResponse doRequest(RpcRequest rpcRequest, ServiceMetaInfo serviceMetaInfo) throws ExecutionException, InterruptedException {

        Vertx vertx = Vertx.vertx();
        NetClient tcpClient = vertx.createNetClient();

        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        tcpClient.connect(serviceMetaInfo.getServicePort(), serviceMetaInfo.getServiceHost(), result -> {
            if (!result.succeeded()){
                log.info("fail to connect tcp server");
                return;
            }
            NetSocket socket = result.result();

            // 发送消息
            ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = new ProtocolMessage<>();
            ProtocolMessage.Header header = new ProtocolMessage.Header();
            header.setMagic(ProtocolConstant.MAGIC);
            header.setVersion(ProtocolConstant.VERSION);
            header.setSerializer((byte) ProtocolMessageSerializerEnum.of(RpcApplication.getRpcConfig().getSerializer()).getKey());
            header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
            // 生成全局请求ID
            header.setRequestId(IdUtil.getSnowflakeNextId());

            rpcRequestProtocolMessage.setHeader(header);
            rpcRequestProtocolMessage.setBody(rpcRequest);

            try {
                Buffer encodeBuffer = ProtocolMessageEncoder.encode(rpcRequestProtocolMessage);
                socket.write(encodeBuffer);
            } catch (IOException e) {
                throw new RuntimeException("协议编码错误");
            }

            TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
                try {
                    ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                    responseFuture.complete(rpcResponseProtocolMessage.getBody());
                } catch (IOException e) {
                    throw new RuntimeException("协议解码错误");
                }
            });
            socket.handler(tcpBufferHandlerWrapper);
        });

        RpcResponse rpcResponse = responseFuture.get();
        tcpClient.close();
        return rpcResponse;
    }


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
