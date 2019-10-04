package com.ljl.note.collection.liveRecord.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LiveRecordTaskStartDTO implements Serializable {
    private static final long serialVersionUID=1l;

    private Long pid;

    private Long roomId;

    private Long storeId;
}
