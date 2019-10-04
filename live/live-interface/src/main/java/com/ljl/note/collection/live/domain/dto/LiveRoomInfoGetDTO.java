package com.ljl.note.collection.live.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by liaura_ljl on 2019/10/1.
 */
@Data
public class LiveRoomInfoGetDTO implements Serializable{
    private static final long serialVersioUID=1l;

    private Long roomId;
}
