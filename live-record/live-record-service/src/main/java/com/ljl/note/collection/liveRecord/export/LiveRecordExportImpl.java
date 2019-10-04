package com.ljl.note.collection.liveRecord.export;

import com.ljl.note.collection.common.utils.SoaResponse;
import com.ljl.note.collection.common.utils.SoaResponseUtil;
import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskEndDTO;
import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskStartDTO;
import com.ljl.note.collection.liveRecord.facade.LiveRecordFacade;
import com.ljl.note.collection.liveRecord.service.LiveRecordExport;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by liaura_ljl on 2019/10/4.
 */
public class LiveRecordExportImpl implements LiveRecordExport {

    @Autowired
    private LiveRecordFacade liveRecordFacade;

    /**
     * 开始录播(开始、恢复直播时调用)
     * @param liveRecordTaskStartDTO
     * @return
     */
    @Override
    public SoaResponse<Boolean, ?> startLiveRecordTask(LiveRecordTaskStartDTO liveRecordTaskStartDTO) {
        return SoaResponseUtil.ok(liveRecordFacade.startLiveRecordTask(liveRecordTaskStartDTO));
    }

    /**
     * 结束录播(暂停、结束直播时调用)
     * @param liveRecordTaskEndDTO
     * @return
     */
    @Override
    public SoaResponse<Boolean, ?> endLiveRecordTask(LiveRecordTaskEndDTO liveRecordTaskEndDTO) {
        return SoaResponseUtil.ok(liveRecordFacade.endLiveRecordTask(liveRecordTaskEndDTO));
    }
}
