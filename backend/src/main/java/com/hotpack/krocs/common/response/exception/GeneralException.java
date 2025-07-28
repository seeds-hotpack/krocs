package com.hotpack.krocs.common.response.exception;

import com.hotpack.krocs.common.response.code.BaseCode;
import com.hotpack.krocs.common.response.code.Reason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseCode errorCode;

    public Reason getErrorReason() {
        return this.errorCode.getReason();
    }

    public Reason getErrorReasonHttpStatus(){
        return this.errorCode.getReasonHttpStatus();
    }
}