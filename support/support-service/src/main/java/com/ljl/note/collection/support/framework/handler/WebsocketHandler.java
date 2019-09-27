package com.ljl.note.collection.support.framework.handler;

import com.ljl.note.collection.support.common.RedisService;
import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;
import com.ljl.note.collection.support.framework.job.AuthQueue;
import com.ljl.note.collection.support.framework.job.AuthTask;
import com.ljl.note.collection.support.common.RedisKeyConstant;
import com.ljl.note.collection.support.framework.util.NettyConnectionUtil;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Sharable
@Slf4j
public class WebsocketHandler extends SelfHandler<TextWebSocketFrame> {
    @Autowired
    private RedisService redisService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        //处理不同类型的数据片段约定
        //入站消息 AUTH_token    INROOM_wid   INLIVEROOM_livecode    OUTLIVEROOM_livecode   OUTROOM_wid
        //出站消息 FRESH_goods   BARGAINACTIV_json  ROOMBARGAIN_3  ORDER_text
        String[] typeAndContent = msg.text().split("_");
        log.info(msg.text());
        if(typeAndContent==null || typeAndContent.length!=2){
            ctx.channel().close();
        }else{
            switch (WebSocketMsgTypeEnum.of(typeAndContent[0])){
                case AUTH:
                    String tokenKey = String.format(RedisKeyConstant.AUTHKEY,typeAndContent[1]);
                    if(tokenKey.equals(redisService.get(tokenKey))){
                    //if(typeAndContent[1].equals("test")){
                         log.info("入站消息：{}",typeAndContent[1]);
                         AuthQueue.removeId(ctx.channel().id().asLongText());
                         NettyConnectionUtil.channelUserMap.put(ctx.channel().id().asLongText(),0L);
                    }else{
                        log.info("权限校验失败 key:{},value:{}",tokenKey,redisService.get(tokenKey));
                    }
                    break;
                case INAPPLET:
                    log.info("进入小程序,wid,{}",typeAndContent[1]);
                    NettyConnectionUtil.userAddInRoom(Long.parseLong(typeAndContent[1]),ctx);
                    break;
                case INLIVEROOM:
                    log.info("进入直播场次：{}",typeAndContent[1]);
                    NettyConnectionUtil.userAddInLiveRoom(typeAndContent[1],ctx);
                    break;
                case OUTLIVEROOM:
                    log.info("退出直播场次：{}",typeAndContent[1]);
                    NettyConnectionUtil.userOutLiveRoom(ctx);
                    break;
                case OUTAPPLET:
                    log.info("退出小程序：{}",typeAndContent[1]);
                    NettyConnectionUtil.userOutRoom(ctx);
                    break;
                case HEART:
                    log.info("心跳检测wid：{}，现存直播间通道数：{}",typeAndContent[1], NettyConnectionUtil.userChannelMap.get(Long.parseLong(typeAndContent[1])));
                    break;
            }
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String channelId = ctx.channel().id().asLongText();
        log.info("通道激活：{}",ctx);
        //加入延时队列 5秒内未作权限校验，删除通道
        if(!AuthQueue.authMap.containsKey(channelId)){
            AuthTask authTask = new AuthTask(5L, ctx);
            AuthQueue.authMap.put(channelId,authTask);
            AuthQueue.delayQueue.offer(authTask);
            log.info("delayQueue content:{}", Arrays.toString(AuthQueue.delayQueue.toArray()));
        }
        super.channelActive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyConnectionUtil.userOutRoom(ctx);
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if(idleStateEvent.state() == IdleState.READER_IDLE){
                log.info("通道：{}，30秒未检测到读事件发生删除通道！",ctx);
                ctx.channel().close();
            }
        }
        super.userEventTriggered(ctx, evt);
    }
}
