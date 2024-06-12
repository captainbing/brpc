package com.abing.rpc.server.tcp;

import com.abing.rpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * @Author CaptainBing
 * @Date 2024/6/12 17:09
 * @Description 对原有RecordParser增强
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {


    private RecordParser recordParser;


    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        this.recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    /**
     * 初始化 RecordParser
     * @param bufferHandler
     * @return
     */
    private RecordParser initRecordParser(Handler<Buffer> bufferHandler){

        RecordParser recordParser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);
        recordParser.setOutput(new Handler<Buffer>() {
            // 是否添加body
            boolean appendBodyFlag = false;
            Buffer resultBuffer = Buffer.buffer();
            @Override
            public void handle(Buffer event) {

                if (!appendBodyFlag){
                    resultBuffer.appendBuffer(event);
                    int bodyLen = event.getInt(ProtocolConstant.BODY_LEN_POSITION);
                    recordParser.fixedSizeMode(bodyLen);
                    appendBodyFlag = true;
                }else {
                    resultBuffer.appendBuffer(event);
                    bufferHandler.handle(resultBuffer);
                    appendBodyFlag = false;
                    recordParser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    resultBuffer = Buffer.buffer();
                }
            }
        });
        return recordParser;
    }
}
