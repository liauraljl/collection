package com.ljl.note.collection.live.export;

import com.ljl.note.collection.common.soa.SoaResponse;
import com.ljl.note.collection.common.utils.SoaResponseUtil;
import com.ljl.note.collection.live.service.PressTestExport;
import org.springframework.stereotype.Service;

/**
 * 压力测试
 */
@Service
public class PressTestExportImpl implements PressTestExport {

    /**
     * 压力测试
     * @return
     */
    @Override
    public SoaResponse<String, ?> pressTest(String data) {
        /*try {
            //模拟处理耗时
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return SoaResponseUtil.ok("press test!!!return:"+data);
    }
}
