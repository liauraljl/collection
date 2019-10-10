package com.ljl.note.collection.liveRecord.service;

import com.ljl.note.collection.liveRecord.domain.dto.LiveRecordTaskStartDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LiveRecordServiceTest {

    @Autowired
    private LiveRecordServiceImpl liveRecordService;

    @Test
    public void startLiveRecordTaskTest(){
        LiveRecordTaskStartDTO liveRecordTaskStartDTO=new LiveRecordTaskStartDTO();
        liveRecordTaskStartDTO.setPid(100L);
        liveRecordTaskStartDTO.setRoomId(100L);
        liveRecordTaskStartDTO.setStoreId(100L);
        liveRecordService.startLiveRecordTask(liveRecordTaskStartDTO);
    }
}
