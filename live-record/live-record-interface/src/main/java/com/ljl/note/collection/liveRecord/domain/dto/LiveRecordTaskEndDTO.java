package com.ljl.note.collection.liveRecord.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LiveRecordTaskEndDTO implements Serializable {

    private static final long serialVersionUID = -1561865022598801575L;

    private Long pid;

    private Long roomId;
}
