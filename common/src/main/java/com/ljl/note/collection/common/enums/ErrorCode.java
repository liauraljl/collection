package com.ljl.note.collection.common.enums;

/**
 * 错误码定义
 *
 */
public enum ErrorCode {
    /***********************
     * 公共返回码
     *********************/
    SUCCESS("000000", "处理成功"),
    FAIL("100001", "处理失败"),
    NULL("100002", "返回值为空:"),;


    private String errorCode;
    private String errorMsg;

    private ErrorCode(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
