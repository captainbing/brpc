package com.abing.rpc.server.codec;

import com.abing.rpc.model.RpcRequest;
import com.abing.rpc.model.RpcResponse;
import com.abing.rpc.protocol.ProtocolConstant;
import com.abing.rpc.protocol.ProtocolMessage;
import com.abing.rpc.protocol.ProtocolMessageSerializerEnum;
import com.abing.rpc.protocol.ProtocolMessageTypeEnum;
import com.abing.rpc.serializer.Serializer;
import com.abing.rpc.serializer.SerializerFactory;
import io.vertx.core.buffer.Buffer;

import java.io.IOException;


/**
 * @Author CaptainBing
 * @Date 2024/6/11 15:12
 * @Description
 */
public class ProtocolMessageDecoder {


    /**
     * 协议报文解码
     * @param buffer
     * @return
     */
    public static ProtocolMessage<?> decode(Buffer buffer) throws IOException {

        ProtocolMessage.Header header = new ProtocolMessage.Header();

        // 校验魔数
        byte magic = buffer.getByte(0);
        if (magic != ProtocolConstant.MAGIC){
            throw new IllegalArgumentException("magic number is illegal");
        }
        header.setMagic(magic);
        header.setVersion(buffer.getByte(1));
        header.setSerializer(buffer.getByte(2));
        header.setType(buffer.getByte(3));
        header.setStatus(buffer.getByte(4));
        header.setRequestId(buffer.getInt(5));
        header.setBodyLength(buffer.getInt(13));

        byte[] dataBytes = buffer.getBytes(ProtocolConstant.MESSAGE_HEADER_LENGTH, ProtocolConstant.MESSAGE_HEADER_LENGTH + header.getBodyLength());

        ProtocolMessageSerializerEnum serializerEnum = ProtocolMessageSerializerEnum.of(header.getSerializer());

        Serializer serializer = SerializerFactory.getInstance(serializerEnum.name());

        ProtocolMessageTypeEnum protocolMessageTypeEnum = ProtocolMessageTypeEnum.of(header.getType());
        switch (protocolMessageTypeEnum){
            case REQUEST:
                RpcRequest rpcRequest = serializer.deserialize(dataBytes, RpcRequest.class);
                return new ProtocolMessage<>(header, rpcRequest);
            case RESPONSE:
                RpcResponse rpcResponse = serializer.deserialize(dataBytes, RpcResponse.class);
                return new ProtocolMessage<>(header, rpcResponse);
            case HEART_BEAT:
                break;
            case OTHER:
                break;
            default:
                throw new IllegalArgumentException("unknown protocol message type");
        }

        return new ProtocolMessage<>();
    }
}
