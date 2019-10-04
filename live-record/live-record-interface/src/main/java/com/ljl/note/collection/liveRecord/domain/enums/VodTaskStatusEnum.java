package com.ljl.note.collection.liveRecord.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum VodTaskStatusEnum {
    Waiting("WAITING","等待中"),
    Processing("PROCESSING","处理中"),
    Finish("FINISH","已完成");

    @Getter
    private String type;

    @Getter
    private String desc;
}
