package com.abing.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.constant.RpcConstant;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.protocol.ProtocolConstant;
import com.abing.rpc.protocol.ProtocolMessage;
import com.abing.rpc.protocol.ProtocolMessageSerializerEnum;
import com.abing.rpc.protocol.ProtocolMessageTypeEnum;
import com.abing.rpc.registry.Registry;
import com.abing.rpc.registry.RegistryFactory;
import com.abing.rpc.serializer.Serializer;
import com.abing.rpc.serializer.SerializerFactory;
import com.abing.rpc.server.codec.ProtocolMessageDecoder;
import com.abing.rpc.server.codec.ProtocolMessageEncoder;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 15:35
 * @Description
 */
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(serviceName)
                                          .methodName(method.getName())
                                          .paramTypes(method.getParameterTypes())
                                          .args(args)
                                          .build();
        // 序列化
        CompletableFuture<RpcResponse> responseFuture = new CompletableFuture<>();

        // 从注册中心获取服务提供者请求地址
        RpcConfig rpcConfig = RpcApplication.getRpcConfig();
        Registry registry = RegistryFactory.getInstance(rpcConfig.getRegistryConfig().getRegistry());
        ServiceMetaInfo serviceMetaInfo = new ServiceMetaInfo();
        serviceMetaInfo.setServiceName(serviceName);
        serviceMetaInfo.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        List<ServiceMetaInfo> serviceMetaInfoList = registry.serviceDiscovery(serviceMetaInfo.getServiceKey());
        if (CollUtil.isEmpty(serviceMetaInfoList)) {
            throw new RuntimeException("暂无服务地址");
        }
        ServiceMetaInfo selectedServiceMetaInfo = serviceMetaInfoList.get(0);

        // 发送TCP请求
        Vertx vertx = Vertx.vertx();
        NetClient tcpClient = vertx.createNetClient();
        tcpClient.connect(selectedServiceMetaInfo.getServicePort(),selectedServiceMetaInfo.getServiceHost(), result -> {
            if (result.succeeded()) {
                System.out.println("connect tcp server");
                NetSocket socket = result.result();

                ProtocolMessage<RpcRequest> rpcRequestProtocolMessage = new ProtocolMessage<>();

                ProtocolMessage.Header header = new ProtocolMessage.Header();

                header.setMagic(ProtocolConstant.MAGIC);
                header.setVersion(ProtocolConstant.VERSION);
                header.setSerializer((byte) ProtocolMessageSerializerEnum.valueOf(RpcApplication.getRpcConfig().getSerializer()).getKey());
                header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
                header.setRequestId(IdUtil.getSnowflakeNextId());

                rpcRequestProtocolMessage.setHeader(header);
                rpcRequestProtocolMessage.setBody(rpcRequest);

                Buffer encodeBuffer = null;
                try {
                    encodeBuffer = ProtocolMessageEncoder.encode(rpcRequestProtocolMessage);
                    socket.write(encodeBuffer);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                // 处理响应
                socket.handler(buffer -> {
                    try {
                        ProtocolMessage<RpcResponse> rpcResponseProtocolMessage = (ProtocolMessage<RpcResponse>) ProtocolMessageDecoder.decode(buffer);
                        responseFuture.complete(rpcResponseProtocolMessage.getBody());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });

            }
        });

        RpcResponse response = responseFuture.get();
        tcpClient.close();
        return response.getData();

    }
}
