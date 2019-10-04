package com.ljl.note.collection.liveRecord.facade;

import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskEndDTO;
import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskStartDTO;
import com.ljl.note.collection.liveRecord.service.LiveRecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

public class LiveRecordFacade {

    @Autowired
    private LiveRecordServiceImpl liveRecordServiceImpl;

    /**
     * 开始录播(开始、恢复直播时调用)
     * @param liveRecordTaskStartDTO
     * @return
     */
    public Boolean startLiveRecordTask(LiveRecordTaskStartDTO liveRecordTaskStartDTO){
        return liveRecordServiceImpl.startLiveRecordTask(liveRecordTaskStartDTO);
    }

    /**
     * 结束录播(暂停、结束直播时调用)
     * @param liveRecordTaskEndDTO
     * @return
     */
    public Boolean endLiveRecordTask(LiveRecordTaskEndDTO liveRecordTaskEndDTO){
        return liveRecordServiceImpl.endLiveRecordTask(liveRecordTaskEndDTO);
    }
}
