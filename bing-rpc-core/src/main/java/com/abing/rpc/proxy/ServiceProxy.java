package com.abing.rpc.proxy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.abing.rpc.RpcApplication;
import com.abing.rpc.config.RpcConfig;
import com.abing.rpc.constant.RpcConstant;
import com.abing.rpc.loadbalance.ConsistentHashLoadBalancer;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.model.ServiceMetaInfo;
import com.abing.rpc.protocol.ProtocolConstant;
import com.abing.rpc.protocol.ProtocolMessage;
import com.abing.rpc.protocol.ProtocolMessageSerializerEnum;
import com.abing.rpc.protocol.ProtocolMessageTypeEnum;
import com.abing.rpc.registry.Registry;
import com.abing.rpc.registry.RegistryFactory;
import com.abing.rpc.retry.RetryStrategy;
import com.abing.rpc.retry.RetryStrategyFactory;
import com.abing.rpc.serializer.Serializer;
import com.abing.rpc.serializer.SerializerFactory;
import com.abing.rpc.server.codec.ProtocolMessageDecoder;
import com.abing.rpc.server.codec.ProtocolMessageEncoder;
import com.abing.rpc.server.tcp.VertxTcpClient;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 15:35
 * @Description
 */
public class ServiceProxy implements InvocationHandler {


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {

        String serviceName = method.getDeclaringClass().getName();
        RpcRequest rpcRequest = RpcRequest.builder()
                                          .serviceName(serviceName)
                                          .methodName(method.getName())
                                          .paramTypes(method.getParameterTypes())
                                          .args(args)
                                          .build();
        try {
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
            // 负载均衡
            Map<String,Object> requestParam = new HashMap<>(1);
            requestParam.put("methodName",rpcRequest.getMethodName());
            ServiceMetaInfo selectedServiceMetaInfo = new ConsistentHashLoadBalancer().select(requestParam, serviceMetaInfoList);

            // 发送TCP请求
            RetryStrategy retryStrategy = RetryStrategyFactory.getInstance(rpcConfig.getRetryStrategy());
            RpcResponse rpcResponse = retryStrategy.doRetry(()-> VertxTcpClient.doRequest(rpcRequest, selectedServiceMetaInfo));

            return rpcResponse.getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
