package com.ljl.note.collection.process.execption;

import com.ljl.note.collection.process.common.MsCloudPlugBaseCode;
import lombok.Getter;

/**
 * @description: MsCloudPlugException
 */
@Getter
public class MsCloudPlugException extends RuntimeException{

    private String errCode;

    private String errMsg;

    public MsCloudPlugException(String errMsg) {
        this(MsCloudPlugBaseCode.FAIL.getCode(), errMsg);
    }

    public MsCloudPlugException(MsCloudPlugBaseCode msCloudPlugBaseCode) {
        this(msCloudPlugBaseCode.getCode(), msCloudPlugBaseCode.getCodeMsg());
    }

    public MsCloudPlugException(String errCode, String errMsg) {
        super(errMsg);
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

}
