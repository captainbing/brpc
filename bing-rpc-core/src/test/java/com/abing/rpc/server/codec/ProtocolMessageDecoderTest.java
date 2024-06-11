package com.abing.rpc.server.codec;

import cn.hutool.core.util.IdUtil;
import com.abing.rpc.constant.RpcConstant;
import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.protocol.*;
import io.vertx.core.buffer.Buffer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;


/**
 * @Author CaptainBing
 * @Date 2024/6/11 16:09
 * @Description
 */
public class ProtocolMessageDecoderTest {

    @Test
    public void decode() throws IOException {

        ProtocolMessage protocolMessage = new ProtocolMessage();


        ProtocolMessage.Header header = new ProtocolMessage.Header();

        header.setMagic(ProtocolConstant.MAGIC);
        header.setVersion(ProtocolConstant.VERSION);
        header.setSerializer((byte) ProtocolMessageSerializerEnum.JDK.getKey());
        header.setType((byte) ProtocolMessageTypeEnum.REQUEST.getKey());
        header.setStatus((byte) ProtocolMessageStatusEnum.OK.getCode());
        header.setRequestId(IdUtil.getSnowflakeNextId());
        header.setBodyLength(0);

        RpcRequest rpcRequest = new RpcRequest();
        rpcRequest.setServiceName("myService");
        rpcRequest.setMethodName("methodName");
        rpcRequest.setServiceVersion(RpcConstant.DEFAULT_SERVICE_VERSION);
        rpcRequest.setParamTypes(new Class[]{String.class});
        rpcRequest.setArgs(new Object[]{"hello"});

        protocolMessage.setHeader(header);
        protocolMessage.setBody(rpcRequest);

        Buffer buffer = ProtocolMessageEncoder.encode(protocolMessage);

        ProtocolMessage<?> message = ProtocolMessageDecoder.decode(buffer);

        Assert.assertNotNull(message);


    }
}