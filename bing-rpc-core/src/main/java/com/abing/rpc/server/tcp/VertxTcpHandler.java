package com.abing.rpc.server.tcp;

import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.protocol.ProtocolMessage;
import com.abing.rpc.protocol.ProtocolMessageStatusEnum;
import com.abing.rpc.protocol.ProtocolMessageTypeEnum;
import com.abing.rpc.registry.LocalRegistry;
import com.abing.rpc.server.codec.ProtocolMessageDecoder;
import com.abing.rpc.server.codec.ProtocolMessageEncoder;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 16:21
 * @Description
 */
public class VertxTcpHandler implements Handler<NetSocket> {

    @Override
    public void handle(NetSocket socket) {


        TcpBufferHandlerWrapper tcpBufferHandlerWrapper = new TcpBufferHandlerWrapper(buffer -> {
            ProtocolMessage<RpcRequest> rpcRequestProtocolMessage;

            try {
                rpcRequestProtocolMessage = (ProtocolMessage<RpcRequest>) ProtocolMessageDecoder.decode(buffer);
            } catch (IOException e) {
                throw new RuntimeException("协议消息解码错误" + e);
            }

            RpcRequest rpcRequest = rpcRequestProtocolMessage.getBody();

            RpcResponse rpcResponse = new RpcResponse();

            try {
                Class<?> implClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method method = implClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object result = method.invoke(implClass.newInstance(), rpcRequest.getArgs());

                rpcResponse.setData(result);
                rpcResponse.setMessage(ProtocolMessageStatusEnum.OK.name());
                rpcResponse.setDataTypes(method.getReturnType());

            } catch (Exception e) {
                rpcResponse.setException(e);
                rpcResponse.setMessage(ProtocolMessageStatusEnum.BAD_RESPONSE.name());
                e.printStackTrace();
            }
            ProtocolMessage.Header header = rpcRequestProtocolMessage.getHeader();

            header.setType((byte) ProtocolMessageTypeEnum.RESPONSE.getKey());
            ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = new ProtocolMessage<>(header, rpcResponse);

            Buffer responseData;
            try {
                responseData = ProtocolMessageEncoder.encode(rpcResponseProtocolMessage);
                socket.write(responseData);
            } catch (IOException e) {
                throw new RuntimeException("协议消息编码错误" + e);
            }
        });


        socket.handler(tcpBufferHandlerWrapper);

    }
}
