package com.ljl.note.collection.imServer.framework.disruptor.consumer;

import com.ljl.note.collection.imCommon.disruptor.MessageConsumer;
import com.ljl.note.collection.imCommon.entity.TranslatorData;
import com.ljl.note.collection.imCommon.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;

/**
 * 服务端消费者
 */
public class ServerMessageConsumer extends MessageConsumer {

    public ServerMessageConsumer(String consumerId){
        super(consumerId);
    }
    @Override
    public void onEvent(TranslatorDataWapper translatorDataWapper) throws Exception {
        TranslatorData data=translatorDataWapper.getData();
        ChannelHandlerContext ctx=translatorDataWapper.getCtx();
        //1.业务处理逻辑:
        System.err.println("Sever端: id= " + data.getId()
                + ", name= " + data.getName()
                + ", message= " + data.getMessage());

        //2.回送响应信息:
        TranslatorData response = new TranslatorData();
        response.setId("resp: " + data.getId());
        response.setName("resp: " + data.getName());
        response.setMessage("resp: " + data.getMessage());
        //写出response响应信息:
        ctx.writeAndFlush(response);
    }
}
