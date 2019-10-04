package com.ljl.note.collection.liveRecord.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LiveRecordCusOpenTypeEnum {
    Off(0,"未开启"),
    On(1,"已开启");

    @Getter
    private final Integer type;

    @Getter
    private final String desc;

    public static String getDesc (Integer type) {
        for (LiveRecordCusOpenTypeEnum e : LiveRecordCusOpenTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e.getDesc();
            }
        }
        return null;
    }


    public static LiveRecordCusOpenTypeEnum of(Integer type) {
        for (LiveRecordCusOpenTypeEnum e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
}
