package com.ljl.note.collection.liveRecord.service;

import com.ljl.note.collection.common.utils.SoaResponse;
import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskEndDTO;
import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskStartDTO;

public interface LiveRecordExport {
    /**
     * 开始录播(开始、恢复直播时调用)
     * @param liveRecordTaskStartDTO
     * @return
     */
    SoaResponse<Boolean,?> startLiveRecordTask(LiveRecordTaskStartDTO liveRecordTaskStartDTO);

    /**
     * 结束录播(暂停、结束直播时调用)
     * @param liveRecordTaskEndDTO
     * @return
     */
    SoaResponse<Boolean,?> endLiveRecordTask(LiveRecordTaskEndDTO liveRecordTaskEndDTO);
}
