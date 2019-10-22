package com.ljl.note.collection.support.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IMRoomDTO implements Serializable {

    private static final long serialVersionUID = -1428261374423570168L;

    private List<String> wids;

    private String messageType;

    private Object content;
}
