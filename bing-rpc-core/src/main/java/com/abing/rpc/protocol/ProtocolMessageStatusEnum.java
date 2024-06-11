package com.abing.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 14:17
 * @Description 协议状态枚举
 */
@AllArgsConstructor
@Getter
public enum ProtocolMessageStatusEnum {


    OK(20),
    CLIENT_TIMEOUT(30),
    SERVER_TIMEOUT(31),
    BAD_REQUEST(40),
    BAD_RESPONSE(50),
    SERVICE_NOT_FOUND(60),
    SERVICE_ERROR(70),
    SERVER_ERROR(80),
    CLIENT_ERROR(90),
    SERVER_THREADPOOL_EXHAUSTED_ERROR(100);

    private final int code;

    /**
     * 根据code获取枚举
     * @param code
     * @return
     */
    public static ProtocolMessageStatusEnum of(int code){

        for (ProtocolMessageStatusEnum value : values()) {
            if(value.code == code){
                return value;
            }
        }
        return null;
    }

}
