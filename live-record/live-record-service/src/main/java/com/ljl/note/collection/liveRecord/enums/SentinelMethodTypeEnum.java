package com.ljl.note.collection.liveRecord.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum SentinelMethodTypeEnum {
    CreateLiveRecordTaskMethod(0,"createLiveRecordTask",100),
    EndLiveRecordTaskMethod(1,"endLiveRecordTask",100),
    SearchMediaIOMethod(2,"searchMediaIO",100),
    EditMediaMethod(3,"editMedia",100),
    DescribeMediaInfosMethod(4,"describeMediaInfos",100),
    DescribeTaskDetailMethod(5,"describeTaskDetail",100),
    DeleteMediaMethod(6,"deleteMedia",100);

    @Getter
    private final Integer type;

    @Getter
    private final String method;

    @Getter
    private final Integer qps;

    public static Integer getQps (Integer type) {
        for (SentinelMethodTypeEnum e : SentinelMethodTypeEnum.values()) {
            if (e.getType().equals(type)) {
                return e.getQps();
            }
        }
        return null;
    }


    public static SentinelMethodTypeEnum of(Integer type) {
        for (SentinelMethodTypeEnum e : values()) {
            if (e.type.equals(type)) {
                return e;
            }
        }
        return null;
    }
}
