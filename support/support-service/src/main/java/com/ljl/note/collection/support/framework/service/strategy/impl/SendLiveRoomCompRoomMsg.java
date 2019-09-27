package com.ljl.note.collection.support.framework.service.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ljl.note.collection.support.framework.service.strategy.SendMessage;
import com.ljl.note.collection.support.framework.util.NettyConnectionUtil;
import com.ljl.note.collection.support.model.msgModel.LiveRoomNotRoomMsg;
import com.ljl.note.collection.support.model.msgModel.WebSocketMsgModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

/**
 * 发送除了正在看直播的人 之外的其他人  消息
 * 无参数
 */
@Slf4j
public class SendLiveRoomCompRoomMsg implements SendMessage {
    @Override
    public Integer sendMsg(String msg) {
        Integer result = 0;
        try{
            WebSocketMsgModel webSocketMsgModel = JSON.parseObject(msg, WebSocketMsgModel.class);
            LiveRoomNotRoomMsg liveRoomNotRoomMsg = JSON.parseObject(JSON.toJSONString(webSocketMsgModel.getData()), LiveRoomNotRoomMsg.class);
            log.info("发送直播间内非场次数据：{}",msg);
            ArrayList<Long> wids = Lists.newArrayList(Sets.difference(NettyConnectionUtil.userChannelMap.keySet(), NettyConnectionUtil.userRoomMap.keySet()));
            wids.stream().forEach(t->{
                TextWebSocketFrame frame = new TextWebSocketFrame(msg.replaceAll("\"","\'"));
                ChannelHandlerContext channelHandlerContext = NettyConnectionUtil.userChannelMap.get(t);
                log.info("通道：{}，wid:{}，发送消息：{}",channelHandlerContext,t,frame.text());
                channelHandlerContext.writeAndFlush(frame);
            });
            result = wids.size();
        }catch (Exception e){
            log.error("发送直播间差集场次人消息异常",e);
        }
        return result;
    }
}
