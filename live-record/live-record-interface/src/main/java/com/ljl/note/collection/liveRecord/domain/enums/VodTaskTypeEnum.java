package com.ljl.note.collection.liveRecord.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum VodTaskTypeEnum {
    EditMedia("EditMedia","视频编辑任务");

    @Getter
    private String type;

    @Getter
    private String desc;

}
