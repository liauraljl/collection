package com.ljl.note.collection.support.framework.util;

import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通道、wid、liveCode绑定关系
 */
@Slf4j
public class NettyConnectionUtil {

    //wid 和 websocket 通道的绑定关系(系统)
    public static Map<Long, ChannelHandlerContext> userChannelMap;
    //channelId 和 wid 的绑定关系  --权限校验成功写入数据
    public static Map<String,Long> channelUserMap;
    //lievCode 和 List<ChannelHandlerContext> 的绑定关系（场次liveCode-通道）
    public static Map<String, List<ChannelHandlerContext>> roomChannelListMap;
    //wid 和 liveCode 的绑定关系
    public static Map<Long,String> userRoomMap;
    //channel 和 liveCode的绑定关系（通道id-场次liveCode）
    public static Map<String,String> channelRoomIdMap;
    static{
        userChannelMap = new ConcurrentHashMap<>();
        channelUserMap = new ConcurrentHashMap<>();
        roomChannelListMap = new ConcurrentHashMap<>();
        channelRoomIdMap = new ConcurrentHashMap<>();
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
        log.info("进入系统：userChannelMap：{}，channelUserMap：{}",
                Arrays.toString(userChannelMap.entrySet().toArray()),
                Arrays.toString(channelUserMap.entrySet().toArray()));
    }

    public static void userAddInLiveRoom(String liveCode,ChannelHandlerContext ctx){
        String channelId = ctx.channel().id().asLongText();
        if(channelUserMap.containsKey(channelId) && userChannelMap.containsKey(channelUserMap.get(channelId))){
            if(roomChannelListMap.containsKey(liveCode)){
                roomChannelListMap.get(liveCode).add(ctx);
            }else{
                ArrayList<ChannelHandlerContext> ctxs = new ArrayList<>();
                ctxs.add(ctx);
                roomChannelListMap.put(liveCode,ctxs);
            }
            channelRoomIdMap.put(channelId,liveCode);
            userRoomMap.put(channelUserMap.get(channelId),liveCode);
        }
        log.info("用户进入直播间：roomChannelListMap：{}，channelRoomId：{},userRoomMap:{}",
                Arrays.toString(roomChannelListMap.entrySet().toArray()),
                Arrays.toString(channelRoomIdMap.entrySet().toArray()),
                Arrays.toString(userRoomMap.entrySet().toArray()));
    }

    public static void userOutLiveRoom(ChannelHandlerContext ctx){
        String channelId = ctx.channel().id().asLongText();
        String liveCode = channelRoomIdMap.remove(channelId);
        if(!StringUtils.isEmpty(liveCode) && !CollectionUtils.isEmpty(roomChannelListMap.get(liveCode))){
            roomChannelListMap.get(liveCode).remove(ctx);
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
