package com.ljl.note.collection.support.framework.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Joiner;
import com.ljl.note.collection.common.utils.IdWorker;
import com.ljl.note.collection.support.common.RedisKeyConstant;
import com.ljl.note.collection.support.common.RedisService;
import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;
import com.ljl.note.collection.support.framework.service.strategy.StrategyContext;
import com.ljl.note.collection.support.framework.util.NettyConnectionUtil;
import jodd.typeconverter.Convert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WebsocketUtilService {

    @Autowired
    private RedisService redisService;

    @Autowired
    private IdWorker idWorker;

    /**
     * 发放token
     * @return
     */
    public String getToken() {
        String token = Convert.toString(idWorker.nextId());
        String key = String.format(RedisKeyConstant.AUTHKEY, token);
        log.info("发放token:{}", token);
        redisService.set(key, token, 10, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 获取用户wid,放入redis（负载均衡）
     *
     * @param redisKey
     * @return
     */
    public Boolean getWidFromWebsocket(String redisKey) {
        Assert.notNull(redisKey, "入参不能为空！");
        String[] split = redisKey.split(":");
        if (split == null || split.length == 0) {
            log.error("格式校验错误！");
            return false;
        }
        List<Long> nows = NettyConnectionUtil.userChannelMap.keySet().stream().collect(Collectors.toList());
        String widString = !CollectionUtils.isEmpty(nows) ? Joiner.on(",").join(nows) : "";
        String redisVal = redisService.get(redisKey);
        String string = StringUtils.isEmpty(redisVal) ? "" : redisVal + ",";
        redisService.set(redisKey, string + widString, 20L, TimeUnit.SECONDS);
        return true;
    }

    //发送砍价消息和下单消息到场次  发送下单消息和模板消息给直播间
    public Integer sendMsgToLiveRoom(WebSocketMsgTypeEnum webSocketMsgTypeEnum, String content){
        log.info("WebSocket发送数据：{}", JSON.toJSONString(content));
        StrategyContext context = new StrategyContext();
        context.setSendMessage(webSocketMsgTypeEnum);
        return context.sendMsg(content);
    }

}
