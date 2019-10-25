package com.ljl.note.collection.imClient.framework.netty.handler;

import com.ljl.note.collection.imCommon.disruptor.MessageProducer;
import com.ljl.note.collection.imCommon.disruptor.RingBufferWorkerPoolFactory;
import com.ljl.note.collection.imCommon.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData response = (TranslatorData)msg;
        String producerId = "code:seesionId:002";
        MessageProducer messageProducer = RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.pushData(response, ctx);
    }
}
