package com.ljl.note.collection.support.framework.service.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;
import com.ljl.note.collection.support.framework.service.strategy.SendMessage;
import com.ljl.note.collection.support.framework.util.NettyConnectionUtil;
import com.ljl.note.collection.support.model.msgModel.RoomMsg;
import com.ljl.note.collection.support.model.msgModel.WebSocketMsgModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;

/**
 * 发送直播间消息
 * 消息格式   liveCode_价钱  liveCode_下单消息
 */
@Slf4j
public class SendRoomMsg implements SendMessage {
    @Override
    public Integer sendMsg(String msg) {
        //消息格式 liveCode_价钱
        Integer result = 0;
        try{
            WebSocketMsgModel webSocketMsgModel = JSON.parseObject(msg, WebSocketMsgModel.class);
            RoomMsg roomMsg = JSON.parseObject(JSON.toJSONString(webSocketMsgModel.getData()), RoomMsg.class);
            Assert.notNull(roomMsg,"直播间场次信息不为空！");
            Assert.notNull(roomMsg.getMsg(),"场次信息不为空！");
            String liveCode = roomMsg.getLiveCode();
            List<ChannelHandlerContext> channelHandlerContexts = NettyConnectionUtil.roomChannelListMap.get(liveCode);
            if(!CollectionUtils.isEmpty(channelHandlerContexts)){
                WebSocketMsgModel<Object> response = new WebSocketMsgModel<Object>(webSocketMsgModel.getMsgType(), roomMsg.getMsg());
                String jsonData = JSON.toJSONString(response);
                log.info("消息类型：{}，消息内容：{}", WebSocketMsgTypeEnum.getByInt(webSocketMsgModel.getMsgType()),jsonData);
                log.info("通道:{},消息：{}", Arrays.toString(channelHandlerContexts.toArray()),jsonData);
                channelHandlerContexts.parallelStream().forEach(t->t.writeAndFlush(new TextWebSocketFrame(jsonData.replaceAll("\"","\'"))));
                result = channelHandlerContexts.size();
            }

        }catch (Exception e){
            log.error("发送直播间场次消息异常",e);
        }
        return result;
    }
}
