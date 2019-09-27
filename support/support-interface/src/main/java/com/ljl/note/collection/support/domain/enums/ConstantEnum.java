package com.ljl.note.collection.support.domain.enums;

import lombok.Getter;

public enum ConstantEnum {
    HEART(0,"HEART"),
    AUTH(1,"AUTH"),
    INROOM(2,"INROOM"),
    INLIVEROOM(3,"INLIVEROOM"),
    OUTLIVEROOM(4,"OUTLIVEROOM"),
    OUTROOM(5,"OUTROOM"),
    /**-----------------华丽分割线-----------------*/
    FRESH(6,"刷新好物榜"),
    BARGAINACTIV(7,"砍价活动提醒"),
    ROOMBARGAIN(8,"直播间砍价"),
    ORDER(9,"直播间下单"),
    ROOMACTIVITYSTAT(10,"直播间活动状态"),
    ROOMACTIVITYRESTPERSON(11,"直播间活动剩余名额");
    @Getter
    int i;
    @Getter
    String s;
    ConstantEnum(int i, String s){
        this.i = i;
        this.s = s;
    }

    public static ConstantEnum of(String s) {
        for (ConstantEnum actionEnum : ConstantEnum.values()) {
            if (actionEnum.s.equals(s) ) {
                return actionEnum;
            }
        }
        return AUTH;
    }

    public static ConstantEnum getByInt(Integer s) {
        for (ConstantEnum actionEnum : ConstantEnum.values()) {
            if (actionEnum.i == s ) {
                return actionEnum;
            }
        }
        return AUTH;
    }
}
