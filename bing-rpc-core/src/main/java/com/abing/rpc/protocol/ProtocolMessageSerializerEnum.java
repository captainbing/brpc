package com.abing.rpc.protocol;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author CaptainBing
 * @Date 2024/6/11 14:29
 * @Description 协议序列化枚举
 */
@AllArgsConstructor
@Getter
public enum ProtocolMessageSerializerEnum {

    JDK(0),
    JSON(1),
    KRYO(2),
    HESSIAN(3);

    private final int key;


    /**
     * 根据key获取协议序列化枚举
     * @param key
     * @return
     */
    public static ProtocolMessageSerializerEnum of(int key){

        for (ProtocolMessageSerializerEnum value : values()) {
            if(value.key == key){
                return value;
            }
        }
        throw new EnumConstantNotPresentException(ProtocolMessageSerializerEnum.class, "序列化器不存在");
    }

    /**
     * 根据枚举名称获取序列化器
     *
     * @param name
     * @return
     */
    public static ProtocolMessageSerializerEnum of(String name){

        for (ProtocolMessageSerializerEnum value : values()) {
            if(value.name() == name){
                return value;
            }
        }
        throw new EnumConstantNotPresentException(ProtocolMessageSerializerEnum.class, "序列化器不存在");
    }


}
