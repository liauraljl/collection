package com.ljl.note.collection.live.service;

import com.ljl.note.collection.common.soa.SoaResponse;
import com.ljl.note.collection.live.domain.bo.LiveRoomInfoBO;
import com.ljl.note.collection.live.domain.dto.LiveRoomInfoGetDTO;

/**
 * Created by liaura_ljl on 2019/10/1.
 */
public interface LiveRoomExport {

    /**
     * 查询直播场次信息
     * @param liveRoomInfoGetDTO
     * @return
     */
    SoaResponse<LiveRoomInfoBO,?> queryRoomByRoomId(LiveRoomInfoGetDTO liveRoomInfoGetDTO);
}
