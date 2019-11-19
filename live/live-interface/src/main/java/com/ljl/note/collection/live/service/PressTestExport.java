package com.ljl.note.collection.live.service;

import com.ljl.note.collection.common.soa.SoaResponse;

/**
 * 压力测试
 */
public interface PressTestExport {

    /**
     * 压力测试
     * @return
     */
    SoaResponse<String,?> pressTest();
}
