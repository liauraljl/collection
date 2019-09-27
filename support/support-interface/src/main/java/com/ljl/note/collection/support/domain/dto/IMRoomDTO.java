package com.ljl.note.collection.support.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IMRoomDTO implements Serializable {
    private List<String> wids;
    private String messageType;
    private Object content;
}
