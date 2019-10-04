package com.ljl.note.collection.liveRecord.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum LiveRecordTaskStatusEnum {
    Processing(0,"录制中"),
    StopRecord(1,"结束录制"),
    Merging(2,"合并中"),
    Finish(3,"成功(合并完成)"),
    Failed(4,"录制失败");

    @Getter
    private final Integer type;

    @Getter
    private final String desc;

    public static String getDesc (Integer type) {
        for (LiveRecordTaskStatusEnum e : LiveRecordTaskStatusEnum.values()) {
            if (e.getType().equals(type)) {
                return e.getDesc();
            }
        }
        return null;
    }


    public static LiveRecordTaskStatusEnum of(Integer type) {
        for (LiveRecordTaskStatusEnum e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
}
