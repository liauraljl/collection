package com.ljl.note.collection.common.utils;

import com.alibaba.dubbo.rpc.RpcException;
import com.ljl.note.collection.common.enums.ErrorCode;

/**
 * Created by liaura_ljl on 2019/8/2.
 */
public class SoaResponseUtil {
    public SoaResponseUtil() {
    }

    public static <E> E unpack(SoaResponse<E, ?> soaResponse) {
        if(0L != Long.valueOf(soaResponse.getReturnCode()).longValue()) {
            throw new RpcException(soaResponse.getReturnMsg());
        } else {
            return soaResponse.getResponseVo();
        }
    }

    public static <E> SoaResponse<E, Void> ok(E responseVo) {
        SoaResponse<E, Void> soaResponse = new SoaResponse();
        soaResponse.setResponseVo(responseVo);
        soaResponse.setReturnCode(ErrorCode.SUCCESS.getErrorCode());
        soaResponse.setReturnMsg(ErrorCode.SUCCESS.getErrorMsg());
        SoaContext.getLocal().clear();
        SoaContext.getGlobal().clear();
        return soaResponse;
    }

    public static <E> SoaResponse<E, Void> error(String returnCode, String returnMsg, E responseVo) {
        SoaResponse<E, Void> soaResponse = new SoaResponse();
        soaResponse.setResponseVo(responseVo);
        soaResponse.setReturnCode(returnCode);
        soaResponse.setReturnMsg(returnMsg);
        SoaContext.getLocal().clear();
        SoaContext.getGlobal().clear();
        return soaResponse;
    }

    public static <E> SoaResponse<E, Void> error(Long returnCode, String returnMsg, E responseVo) {
        SoaResponse<E, Void> soaResponse = new SoaResponse();
        soaResponse.setResponseVo(responseVo);
        soaResponse.setReturnCode(returnCode.toString());
        soaResponse.setReturnMsg(returnMsg);
        SoaContext.getLocal().clear();
        SoaContext.getGlobal().clear();
        return soaResponse;
    }
}
