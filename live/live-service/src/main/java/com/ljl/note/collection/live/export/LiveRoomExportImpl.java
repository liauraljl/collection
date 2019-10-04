package com.ljl.note.collection.live.export;

import com.ljl.note.collection.common.utils.SoaResponse;
import com.ljl.note.collection.live.domain.bo.LiveRoomInfoBO;
import com.ljl.note.collection.live.domain.dto.LiveRoomInfoGetDTO;
import com.ljl.note.collection.live.service.LiveRoomExport;

import java.io.Serializable;

/**
 * Created by liaura_ljl on 2019/10/4.
 */
public class LiveRoomExportImpl implements LiveRoomExport {
    /**
     * 查询直播场次信息
     * @param liveRoomInfoGetDTO
     * @return
     */
    @Override
    public SoaResponse<LiveRoomInfoBO, ?> queryRoomByRoomId(LiveRoomInfoGetDTO liveRoomInfoGetDTO) {
        return null;
    }
}
