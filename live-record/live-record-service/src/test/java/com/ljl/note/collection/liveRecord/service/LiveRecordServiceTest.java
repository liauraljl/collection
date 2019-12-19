package com.ljl.note.collection.liveRecord.service;

import com.alibaba.fastjson.JSON;
import com.ljl.note.collection.common.utils.SoaResponseUtil;
import com.ljl.note.collection.live.domain.bo.LiveRoomInfoBO;
import com.ljl.note.collection.live.domain.dto.LiveRoomInfoGetDTO;
import com.ljl.note.collection.live.service.LiveRoomExport;
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

    @Autowired
    private LiveRoomExport liveRoomExport;

    @Test
    public void startLiveRecordTaskTest(){
        LiveRecordTaskStartDTO liveRecordTaskStartDTO=new LiveRecordTaskStartDTO();
        liveRecordTaskStartDTO.setPid(100L);
        liveRecordTaskStartDTO.setRoomId(100L);
        liveRecordTaskStartDTO.setStoreId(100L);
        liveRecordService.startLiveRecordTask(liveRecordTaskStartDTO);
    }

    @Test
    public void test1(){
        LiveRoomInfoGetDTO liveRoomInfoGetDTO=new LiveRoomInfoGetDTO();
        liveRoomInfoGetDTO.setRoomId(1L);
        LiveRoomInfoBO liveRoomInfoBO=SoaResponseUtil.unpack(liveRoomExport.queryRoomByRoomId(liveRoomInfoGetDTO));
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(JSON.toJSONString(liveRoomInfoBO));
    }

}
