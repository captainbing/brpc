package com.abing.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 14:24
 * @Description 协议类型枚举
 */
@AllArgsConstructor
@Getter
public enum ProtocolMessageTypeEnum {


    REQUEST(0),
    RESPONSE(1),
    HEART_BEAT(2),
    OTHER(3);

    private final int key;

    /**
     * 根据key获取协议类型
     * @param key
     * @return
     */
    public static ProtocolMessageTypeEnum of(int key){

        for (ProtocolMessageTypeEnum value : values()) {
            if(value.key == key){
                return value;
            }
        }
        throw new EnumConstantNotPresentException(ProtocolMessageTypeEnum.class, "协议消息类型不存在");
    }

}
