package com.ljl.note.collection.support.facade;

import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;
import com.ljl.note.collection.support.framework.service.WebsocketUtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportToolsFacade {

    @Autowired
    private WebsocketUtilService websocketUtilService;

    /**
     * 获取token
     * @return
     */
    public String getToken(){
        return websocketUtilService.getToken();
    }

    /**
     * 发送消息到websocket
     * @param webSocketMsgTypeEnum
     * @param content
     * @return
     */
    public Integer sendMsgToWebsocket(WebSocketMsgTypeEnum webSocketMsgTypeEnum, String content) {
        return websocketUtilService.sendMsgToLiveRoom(webSocketMsgTypeEnum, content);
    }

    /**
     * 获取用户wid,放入redis（负载均衡）
     *
     * @param redisKey
     * @return
     */
    public Boolean getWidFromWebsocket(String redisKey) {
        return websocketUtilService.getWidFromWebsocket(redisKey);
    }
}
