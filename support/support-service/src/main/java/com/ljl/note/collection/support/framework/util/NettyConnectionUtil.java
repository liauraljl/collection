package com.ljl.note.collection.support.framework.util;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.spi.CopyOnWrite;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 通道、wid、liveCode绑定关系
 */
@Slf4j
public class NettyConnectionUtil {

    //wid 和 websocket 通道的绑定关系
    public static Map<Long, ChannelHandlerContext> userChannelMap;//小程序
    //channel 和 wid 的绑定关系  --权限校验成功写入数据
    public static Map<String,Long> channelUserMap;//权限
    //lievCode 和 channel 的绑定关系
    public static Map<String, List<ChannelHandlerContext>> roomChannelId;//场次liveCode-通道
    //wid 和 liveCode 的绑定关系
    public static Map<Long,String> userRoomMap;//wid-场次liveCode
    //channel 和 liveCode的绑定关系
    public static Map<String,String> channelRoomId;//通道id-场次liveCode
    static{
        userChannelMap = new ConcurrentHashMap<>();
        channelUserMap = new ConcurrentHashMap<>();
        roomChannelId = new ConcurrentHashMap<>();
        channelRoomId = new ConcurrentHashMap<>();
        userRoomMap = new ConcurrentHashMap<>();
    }

    public static void userAddInRoom(Long wid,ChannelHandlerContext context){
        if(!userChannelMap.containsKey(wid) && channelUserMap.containsKey(context.channel().id().asLongText())){
            userChannelMap.put(wid,context);
            channelUserMap.put(context.channel().id().asLongText(),wid);
        }else if(userChannelMap.containsKey(wid)){
            //重新构建改用用户的通道映射关系
            userOutRoom(userChannelMap.get(wid));
            userChannelMap.put(wid,context);
            channelUserMap.put(context.channel().id().asLongText(),wid);
        }
        log.info("进入小程序：userChannelMap：{}，channelUserMap：{}",
                Arrays.toString(userChannelMap.entrySet().toArray()),
                Arrays.toString(channelUserMap.entrySet().toArray()));
    }

    public static void userAddInLiveRoom(String liveCode,ChannelHandlerContext ctx){
        String channelId = ctx.channel().id().asLongText();
        if(channelUserMap.containsKey(channelId) && userChannelMap.containsKey(channelUserMap.get(channelId))){
            if(roomChannelId.containsKey(liveCode)){
                roomChannelId.get(liveCode).add(ctx);
            }else{
                ArrayList<ChannelHandlerContext> ctxs = new ArrayList<>();
                ctxs.add(ctx);
                roomChannelId.put(liveCode,ctxs);
            }
            channelRoomId.put(channelId,liveCode);
            userRoomMap.put(channelUserMap.get(channelId),liveCode);
        }
        log.info("用户进入直播间场次：roomChannelId：{}，channelRoomId：{},userRoomMap:{}",
                Arrays.toString(roomChannelId.entrySet().toArray()),
                Arrays.toString(channelRoomId.entrySet().toArray()),
                Arrays.toString(userRoomMap.entrySet().toArray()));
    }

    public static void userOutLiveRoom(ChannelHandlerContext ctx){
        String channelId = ctx.channel().id().asLongText();
        String liveCode = channelRoomId.remove(channelId);
        if(!StringUtils.isEmpty(liveCode) && !CollectionUtils.isEmpty(roomChannelId.get(liveCode))){
            roomChannelId.get(liveCode).remove(ctx);
        }
        userRoomMap.remove(channelUserMap.get(channelId));
    }

    public static void userOutRoom(ChannelHandlerContext ctx){
        String channelId = ctx.channel().id().asLongText();
        userOutLiveRoom(ctx);
        Long remove = channelUserMap.remove(channelId);
        if(remove != null){
            userChannelMap.remove(remove);
        }
        ctx.channel().close();
    }
}
