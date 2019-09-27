package com.ljl.note.collection.support.framework.service;

import com.alibaba.fastjson.JSON;
import com.ljl.note.collection.support.domain.enums.ConstantEnum;
import com.ljl.note.collection.support.framework.service.strategy.StrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendMessageService {

    //发送砍价消息和下单消息到场次  发送下单消息和模板消息给直播间
    public Integer sendMsgToLiveRoom(ConstantEnum constantEnum, String content){
        log.info("WebSocket发送数据：{}", JSON.toJSONString(content));
        StrategyContext context = new StrategyContext();
        context.setSendMessage(constantEnum);
        return context.sendMsg(content);
    }


}
