package com.ljl.note.collection.liveRecord.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 录播任务类型
 */
@AllArgsConstructor
public enum LiveRecordTaskTypeEnum {
    MergeLiveRecordFromListTask(0,"mergeLiveRecordFromListTask"),
    GetMergeResultFromListTask(1,"getMergeResultFromListTask");

    @Getter
    private final Integer type;

    @Getter
    private final String desc;

    public static String getDesc (Integer type) {
        for (LiveRecordTaskTypeEnum e : LiveRecordTaskTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e.getDesc();
            }
        }
        return null;
    }


    public static LiveRecordTaskTypeEnum of(Integer type) {
        for (LiveRecordTaskTypeEnum e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
}
