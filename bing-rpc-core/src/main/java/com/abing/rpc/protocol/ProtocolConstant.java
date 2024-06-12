package com.abing.rpc.protocol;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 14:11
 * @Description
 */
public interface ProtocolConstant {


    /**
     * 消息头长度
     */
    int MESSAGE_HEADER_LENGTH = 17;
    /**
     * 魔数
     */
    byte MAGIC = 0x03;
    /**
     * 版本号
     */
    byte VERSION = 0x01;
    /**
     * 请求体长度
     */
    int BODY_LEN_POSITION = 13;

}
