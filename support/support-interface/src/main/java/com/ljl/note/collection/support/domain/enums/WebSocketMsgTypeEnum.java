package com.ljl.note.collection.support.domain.enums;

import lombok.Getter;

public enum WebSocketMsgTypeEnum {
    HEART(0,"HEART"),
    AUTH(1,"AUTH"),
    INAPPLET(2,"INAPPLET"),
    INLIVEROOM(3,"INLIVEROOM"),
    OUTLIVEROOM(4,"OUTLIVEROOM"),
    OUTAPPLET(5,"OUTAPPLET"),
    /**-----------------华丽分割线-----------------*/
    FRESH(6,"刷新好物榜"),
    BARGAINACTIV(7,"砍价活动提醒"),
    ROOMBARGAIN(8,"直播间砍价"),
    ORDER(9,"直播间下单"),
    ROOMACTIVITYSTAT(10,"直播间活动状态"),
    ROOMACTIVITYRESTPERSON(11,"直播间活动剩余名额");
    @Getter
    private int type;
    @Getter
    private String desc;
    WebSocketMsgTypeEnum(int type, String desc){
        this.type = type;
        this.desc = desc;
    }

    public static WebSocketMsgTypeEnum of(String s) {
        for (WebSocketMsgTypeEnum actionEnum : WebSocketMsgTypeEnum.values()) {
            if (actionEnum.desc.equals(s) ) {
                return actionEnum;
            }
        }
        return AUTH;
    }

    public static WebSocketMsgTypeEnum getByInt(int type) {
        for (WebSocketMsgTypeEnum actionEnum : WebSocketMsgTypeEnum.values()) {
            if (actionEnum.type == type ) {
                return actionEnum;
            }
        }
        return AUTH;
    }
}
