package com.ljl.note.collection.imClient.framework.disruptor.consumer;

import com.ljl.note.collection.imCommon.disruptor.MessageConsumer;
import com.ljl.note.collection.imCommon.entity.TranslatorData;
import com.ljl.note.collection.imCommon.entity.TranslatorDataWapper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.ReferenceCountUtil;

/**
 * 客户端消费者
 */
public class ClientMessageConsumer extends MessageConsumer {

    public ClientMessageConsumer(String consumerId){
        super(consumerId);
    }

    @Override
    public void onEvent(TranslatorDataWapper translatorDataWapper) throws Exception {
        TranslatorData response = translatorDataWapper.getData();
        ChannelHandlerContext ctx = translatorDataWapper.getCtx();
        //业务逻辑处理:
        try {
            System.err.println("Client端: id= " + response.getId()
                    + ", name= " + response.getName()
                    + ", message= " + response.getMessage());
        } finally {
            ReferenceCountUtil.release(response);
        }
    }

}
