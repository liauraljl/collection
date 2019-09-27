package com.ljl.note.collection.common.utils;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * <li>通用SOA响应处理对象</li>
 **/
@Data
public class SoaResponse<T, ErrT> implements Serializable {
    private final static String RETURN_SUCCESS = "000000";
    private static final long serialVersionUID = 1L;
    /** 通用返回码 */
    private String returnCode = RETURN_SUCCESS;
    /** 通用返回 */
    private String returnMsg;
    /** 通用业务信息 */
    private String logBizData;

    private Boolean processResult = true;

    /** 通用成功响应对象 */
    private T responseVo;

    private ErrT errT;

    private String monitorTrackId = UUID.randomUUID().toString();

    /**
     * 时间戳(毫秒)
     */
    private String timestamp = System.currentTimeMillis() + "";

    private String globalTicket;

}
