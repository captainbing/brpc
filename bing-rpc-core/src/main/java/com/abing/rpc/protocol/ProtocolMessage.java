package com.abing.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 14:02
 * @Description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProtocolMessage<T> {


    /**
     * 请求头
     */
    private Header header;
    /**
     * 请求体
     */
    private T body;


    @Data
    public static class Header{

        /**
         * 魔数
         */
        private byte magic;
        /**
         * 版本号
         */
        private byte version;
        /**
         * 序列化器
         */
        private byte serializer;
        /**
         * 消息类型（请求/响应）
         */
        private byte type;
        /**
         * 状态
         */
        private byte status;
        /**
         * 请求Id
         */
        private long requestId;
        /**
         * 数据长度
         */
        private int bodyLength;

    }

}
