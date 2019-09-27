package com.ljl.note.collection.support.framework.service.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ljl.note.collection.support.domain.dto.IMRoomDTO;
import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;
import com.ljl.note.collection.support.framework.service.strategy.SendMessage;
import com.ljl.note.collection.support.framework.util.NettyConnectionUtil;
import com.ljl.note.collection.support.model.msgModel.WebSocketMsgModel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 发送直播间消息,提醒消息
 * 消息格式 MsgType_json
 */
@Slf4j
@Service
public class SendLiveRoomMsg implements SendMessage {

    @Override
    public Integer sendMsg(String msg) {
        Integer result = 0;
        try{
            WebSocketMsgModel webSocketMsgModel = JSON.parseObject(msg, WebSocketMsgModel.class);
            IMRoomDTO imRoomRequest = JSON.parseObject(JSON.toJSONString(webSocketMsgModel.getData()), IMRoomDTO.class);
            List<String> wids = imRoomRequest.getWids();
            log.info("发送直播间数据：{},现存的通道数：{}",JSON.toJSON(wids),JSON.toJSON(NettyConnectionUtil.userChannelMap.entrySet()));
            int size = NettyConnectionUtil.userChannelMap.size();
            if(!CollectionUtils.isEmpty(wids) && size>0){
                result = wids.size();
                wids.stream().forEach(t->{
                    Long wid = Long.parseLong(t);
                    log.info("身份wid:{}",wid);
                    if(NettyConnectionUtil.userChannelMap.containsKey(wid)){
                        if(webSocketMsgModel.getMsgType().equals(WebSocketMsgTypeEnum.BARGAINACTIV.getType()) && NettyConnectionUtil.userRoomMap.containsKey(wid)){
                            String liveCode = NettyConnectionUtil.userRoomMap.get(wid);
                            log.info("当前用户所在的直播场次：wid：{}，liveCode:{}",wid,liveCode);
                            //wid在存在liveCode
                            List<String> liveCodeList = Lists.newArrayList();
                            /*ActivityStartScoketMsgBO activityStartScoketMsgBO = JSON.parseObject(JSON.toJSONString(imRoomRequest.getContent()), ActivityStartScoketMsgBO.class);
                            for(int i=(activityStartScoketMsgBO.getActivityList().size()-1);i>=0;i--){
                                List<ActivityStartScoketMsgBO.LiveRoomAndGoodsInfo> activityList = activityStartScoketMsgBO.getActivityList();
                                List<ActivityStartScoketMsgBO.ActivityGoodsInfo> goodsList = activityList.get(i).getGoodsList();
                                if(CollectionUtils.isNotEmpty(goodsList)){
                                    for(int j=(goodsList.size()-1);j>=0;j--){
                                        ActivityStartScoketMsgBO.ActivityGoodsInfo goodsInfo = goodsList.get(j);
                                        liveCodeList.add(goodsInfo.getLiveCode());
                                        if(liveCode.equals(goodsInfo.getLiveCode())){
                                            goodsList.remove(goodsInfo);
                                        }
                                    }
                                }
                                if(CollectionUtils.isEmpty(goodsList)){
                                    activityList.remove(i);
                                }
                            }*/
                            if(liveCodeList.size()==1 && liveCodeList.contains(liveCode)){
                                log.info("待提醒的直播间正是用户所在的直播间！");
                                return;
                            }else{
                                /*log.info("处理之后的直播间数据：{}",JSON.toJSONString(activityStartScoketMsgBO));
                                this.sendMsg(webSocketMsgModel,activityStartScoketMsgBO,wid);*/
                                return;
                            }
                        }
                        this.sendMsg(webSocketMsgModel,imRoomRequest,wid);
                    }
                });
            }
        }catch (Exception e){
            log.error("发送直播间场次消息异常",e);
        }
        return result;
    }

    private void sendMsg(WebSocketMsgModel webSocketMsgModel,Object object,Long wid){
        WebSocketMsgModel<Object> response = null;
        if(object instanceof IMRoomDTO){
            response = new WebSocketMsgModel(webSocketMsgModel.getMsgType(), ((IMRoomDTO) object).getContent());
        }else{
            response = new WebSocketMsgModel(webSocketMsgModel.getMsgType(), object);
        }
        String jsonData = JSON.toJSONString(response);
        log.info("消息类型：{}，消息内容：{}", WebSocketMsgTypeEnum.getByInt(webSocketMsgModel.getMsgType()),jsonData);
        log.info("通过通道：{}，wid:{}，发送：{}",NettyConnectionUtil.userChannelMap.get(wid),wid,jsonData);
        NettyConnectionUtil.userChannelMap.get(wid).writeAndFlush(new TextWebSocketFrame(jsonData.replaceAll("\"","\'")));
    }
}
