package com.ljl.note.collection.common.exception;

import com.ljl.note.collection.common.enums.BaseErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BaseException extends RuntimeException {

    private static final long serialVersionUID = 3363375694718994058L;
    private BaseErrorCode baseErrorCode;
    protected Long errcode;
    protected String errmsg;


    public BaseException(Throwable throwable) {
        super(throwable);
        this.baseErrorCode = BaseErrorCode.FAILED;
        this.errcode = BaseErrorCode.FAILED.getCode();
        this.errmsg = BaseErrorCode.FAILED.getMsg();
    }

    public BaseException(BaseErrorCode baseErrorCode) {
        this(baseErrorCode.getCode(), baseErrorCode.getMsg());
        this.baseErrorCode = baseErrorCode;
        this.errcode = baseErrorCode.getCode();
        this.errmsg = baseErrorCode.getMsg();
    }

    private BaseException(String errorMsg, Object... params) {
        this.baseErrorCode = BaseErrorCode.FAILED;
        this.errcode = BaseErrorCode.FAILED.getCode();
        this.errmsg = String.format(errorMsg, params);
    }

    public BaseException(Long code, String msg) {
        super(msg);
        this.errmsg = msg;
        this.errcode = code;
    }

    public static BaseException of(Throwable throwable) {
        return new BaseException(throwable);
    }

    public static BaseException of(BaseErrorCode baseErrorCode) {
        return new BaseException(baseErrorCode);
    }

    public static BaseException of(String errorMsg, Object... params) {
        return new BaseException(errorMsg, params);
    }

}
