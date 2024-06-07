package com.abing.rpc.server.http;

import com.abing.rpc.RpcApplication;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.registry.LocalRegistry;
import com.abing.rpc.serializer.Serializer;
import com.abing.rpc.serializer.SerializerFactory;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @Author CaptainBing
 * @Date 2024/5/14 15:10
 * @Description
 */
public class HttpServerHandler implements Handler<HttpServerRequest> {


    @Override
    public void handle(HttpServerRequest request) {

        final Serializer serializer = SerializerFactory.getInstance(RpcApplication.getRpcConfig().getSerializer());

        request.bodyHandler(body -> {
            RpcRequest rpcRequest = null;

            System.out.println("Vertx Server 收到请求: " + request.uri() + " - " + request.method());

            try {
                rpcRequest = serializer.deserialize(body.getBytes(), RpcRequest.class);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            RpcResponse rpcResponse = new RpcResponse();
            if (rpcRequest == null) {
                rpcResponse.setMessage("RpcRequest == null");
                doResponse(request,rpcResponse,serializer);
                return;
            }
            try {
                Class<?> serviceClass = LocalRegistry.get(rpcRequest.getServiceName());
                Method serviceClassMethod = serviceClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object result = serviceClassMethod.invoke(serviceClass.newInstance(), rpcRequest.getArgs());

                rpcResponse.setData(result);
                rpcResponse.setDataTypes(serviceClassMethod.getReturnType());
                rpcResponse.setMessage("ok");

            } catch (Exception e) {
                e.printStackTrace();
                rpcResponse.setMessage(e.getMessage());
                rpcResponse.setException(e);
            }
            doResponse(request,rpcResponse,serializer);
        });
    }


    /**
     * 返回响应
     * @param request
     * @param rpcResponse
     * @param serializer
     */
    private void doResponse(HttpServerRequest request, RpcResponse rpcResponse, Serializer serializer) {

        HttpServerResponse response = request.response()
                                             .putHeader("content-type", "application/json");

        System.out.println("服务器返回信息 = " + response);
        try {
            byte[] dataByte = serializer.serialize(rpcResponse);
            response.end(Buffer.buffer(dataByte));
        } catch (IOException e) {
            e.printStackTrace();
            response.end(Buffer.buffer());
        }


    }
}
