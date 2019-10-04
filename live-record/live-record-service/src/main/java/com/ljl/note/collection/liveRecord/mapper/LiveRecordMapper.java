package com.ljl.note.collection.liveRecord.mapper;


import com.ljl.note.collection.common.mapper.CommonMapper;
import com.ljl.note.collection.liveRecord.model.LiveRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LiveRecordMapper extends CommonMapper<LiveRecord> {

    int deleteRecordByIds(@Param("liveRecordIds") List<Long> liveRecordIds);

}