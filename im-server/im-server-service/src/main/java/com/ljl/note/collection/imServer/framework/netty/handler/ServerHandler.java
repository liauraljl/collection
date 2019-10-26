package com.ljl.note.collection.imServer.framework.netty.handler;

import com.ljl.note.collection.imCommon.disruptor.MessageProducer;
import com.ljl.note.collection.imCommon.disruptor.RingBufferWorkerPoolFactory;
import com.ljl.note.collection.imCommon.entity.TranslatorData;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ServerHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        TranslatorData data=(TranslatorData)msg;
        String producerId="code:producerId:server";
        MessageProducer messageProducer= RingBufferWorkerPoolFactory.getInstance().getMessageProducer(producerId);
        messageProducer.pushData(data,ctx);
    }
}
