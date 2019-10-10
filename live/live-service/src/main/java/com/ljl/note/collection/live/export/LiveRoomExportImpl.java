package com.ljl.note.collection.live.export;

import com.ljl.note.collection.common.soa.SoaResponse;
import com.ljl.note.collection.common.utils.SoaResponseUtil;
import com.ljl.note.collection.live.domain.bo.LiveRoomInfoBO;
import com.ljl.note.collection.live.domain.dto.LiveRoomInfoGetDTO;
import com.ljl.note.collection.live.service.LiveRoomExport;
import org.springframework.stereotype.Service;

/**
 * Created by liaura_ljl on 2019/10/4.
 */
@Service
public class LiveRoomExportImpl implements LiveRoomExport {
    /**
     * 查询直播场次信息
     * @param liveRoomInfoGetDTO
     * @return
     */
    @Override
    public SoaResponse<LiveRoomInfoBO, ?> queryRoomByRoomId(LiveRoomInfoGetDTO liveRoomInfoGetDTO) {
        LiveRoomInfoBO liveRoomInfoBO=new LiveRoomInfoBO();
        liveRoomInfoBO.setTitle("测试");
        liveRoomInfoBO.setRemark("remark");
        return SoaResponseUtil.ok(liveRoomInfoBO);
    }
}
