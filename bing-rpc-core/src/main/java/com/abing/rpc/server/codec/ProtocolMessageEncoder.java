package com.abing.rpc.server.codec;

import com.abing.rpc.protocol.ProtocolMessage;
import com.abing.rpc.protocol.ProtocolMessageSerializerEnum;
import com.abing.rpc.serializer.Serializer;
import com.abing.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;


/**
 * @Author CaptainBing
 * @Date 2024/6/11 15:11
 * @Description
 */
public class ProtocolMessageEncoder {

    /**
     * 协议数据加密
     */
    public static Buffer encode(ProtocolMessage<?> protocolMessage) throws IOException {

        ProtocolMessage.Header messageHeader = protocolMessage.getHeader();

        Buffer buffer = Buffer.buffer();
        buffer.appendByte(messageHeader.getMagic());
        buffer.appendByte(messageHeader.getVersion());
        buffer.appendByte(messageHeader.getSerializer());
        buffer.appendByte(messageHeader.getType());
        buffer.appendByte(messageHeader.getStatus());
        buffer.appendLong(messageHeader.getRequestId());

        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.of(messageHeader.getSerializer());
        Serializer serializer = SerializerFactory.getInstance(serializerEnum.name());
        byte[] dataBytes = serializer.serialize(protocolMessage.getBody());

        buffer.appendInt(dataBytes.length);
        buffer.appendBytes(dataBytes);
        return buffer;
    }
}
