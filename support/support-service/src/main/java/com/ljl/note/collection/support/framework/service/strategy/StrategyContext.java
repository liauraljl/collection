package com.ljl.note.collection.support.framework.service.strategy;

import com.ljl.note.collection.support.domain.enums.WebSocketMsgTypeEnum;
import com.ljl.note.collection.support.framework.service.strategy.impl.SendLiveRoomCompRoomMsg;
import com.ljl.note.collection.support.framework.service.strategy.impl.SendLiveRoomMsg;
import com.ljl.note.collection.support.framework.service.strategy.impl.SendRoomMsg;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class StrategyContext {
    private SendMessage sendMessage;

    public void setSendMessage(WebSocketMsgTypeEnum webSocketMsgTypeEnum){
        switch (webSocketMsgTypeEnum){
            case FRESH: this.sendMessage = new SendLiveRoomCompRoomMsg(); break;
            case BARGAINACTIV: this.sendMessage = new SendLiveRoomMsg(); break;
            case ROOMBARGAIN:
            case ROOMACTIVITYSTAT:
            case ORDER: this.sendMessage = new SendRoomMsg(); break;
        }
    }

    public Integer sendMsg(String content){
        return sendMessage.sendMsg(content);
    }

}
