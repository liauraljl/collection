package com.ljl.note.collection.support.export;

import com.ljl.note.collection.common.soa.SoaResponse;
import com.ljl.note.collection.common.utils.SoaResponseUtil;
import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;
import com.ljl.note.collection.support.facade.SupportToolsFacade;
import com.ljl.note.collection.support.service.SupportToolsExport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SupportToolsExportImpl implements SupportToolsExport {
    @Autowired
    private SupportToolsFacade supportToolsFacade;

    /**
     * 获取token
     * @return
     */
    @Override
    public SoaResponse<String,?> getToken(){
        return SoaResponseUtil.ok(supportToolsFacade.getToken());
    }

    /**
     * 发送消息到websocket
     * @param webSocketMsgTypeEnum
     * @param content
     * @return
     */
    @Override
    public SoaResponse<Integer, ?> sendMsgToWebsocket(WebSocketMsgTypeEnum webSocketMsgTypeEnum,String content) {
        return SoaResponseUtil.ok(supportToolsFacade.sendMsgToWebsocket(webSocketMsgTypeEnum, content));
    }

    /**
     * 获取用户wid,放入redis（负载均衡）
     *
     * @param redisKey
     * @return
     */
    public SoaResponse<Boolean,?> getWidFromWebsocket(String redisKey) {
        return SoaResponseUtil.ok(supportToolsFacade.getWidFromWebsocket(redisKey));
    }
}
