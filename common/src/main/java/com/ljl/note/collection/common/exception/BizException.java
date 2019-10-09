package com.ljl.note.collection.common.exception;

import com.ljl.note.collection.common.enums.BaseErrorCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * description: LiveBizException -  .
 * date:  2018/3/19
 *
 * @author xzc
 */
@Getter
@Setter
@ToString
public class BizException extends BaseException {
    private static final long serialVersionUID = 7886362662694010438L;

    private BaseErrorCode baseErrorCode;
    private Long errcode;
    private String errmsg;

    public BizException(Throwable throwable) {
        super(throwable);
        this.baseErrorCode = BaseErrorCode.FAILED;
        this.errcode = BaseErrorCode.FAILED.getCode();
        this.errmsg = BaseErrorCode.FAILED.getMsg();
    }

    public BizException(BaseErrorCode baseErrorCode) {
        this(baseErrorCode.getCode(), baseErrorCode.getMsg());
        this.baseErrorCode = baseErrorCode;
    }

    public BizException(Long code, String msg) {
        super(code, msg);
        this.errmsg = msg;
        this.errcode = code;
    }

    public BizException(BaseErrorCode baseErrorCode, Long code, String msg) {
        this(code, msg);
        this.baseErrorCode = baseErrorCode;
    }


}
