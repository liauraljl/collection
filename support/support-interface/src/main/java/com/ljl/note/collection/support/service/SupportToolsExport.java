package com.ljl.note.collection.support.service;

import com.ljl.note.collection.common.utils.SoaResponse;
import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;

/**
 * websocket
 */
public interface SupportToolsExport {
    /**
     * 获取token
     * @return
     */
    SoaResponse<String,?> getToken();

    /**
     * 发送消息到websocket
     * @param webSocketMsgTypeEnum
     * @param content
     * @return
     */
    SoaResponse<Integer, ?> sendMsgToWebsocket(WebSocketMsgTypeEnum webSocketMsgTypeEnum, String content);

    /**
     * 获取用户wid,放入redis（负载均衡）
     *
     * @param redisKey
     * @return
     */
    SoaResponse<Boolean,?> getWidFromWebsocket(String redisKey);
}
