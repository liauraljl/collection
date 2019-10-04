package com.ljl.note.collection.liveRecord.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum FluxCountTypeEnum {
    All(-1,"全部"),
    Live(0,"直播消耗"),
    LiveRecord(1,"回放消耗"),
    FluxBuy(2,"流量购买"),
    PackageExpired(3,"套餐到期"),
    PackageUpgrade(4,"套餐升级");

    @Getter
    private final Integer type;

    @Getter
    private final String desc;

    public static String getDesc (Integer type) {
        for (FluxCountTypeEnum e : FluxCountTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e.getDesc();
            }
        }
        return null;
    }


    public static FluxCountTypeEnum of(Integer type) {
        for (FluxCountTypeEnum e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
}
