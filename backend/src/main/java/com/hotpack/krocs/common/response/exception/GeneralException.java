package com.hotpack.krocs.common.response.exception;

import com.hotpack.krocs.common.response.code.BaseErrorCode;
import com.hotpack.krocs.common.response.code.ErrorReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private BaseErrorCode errorCode;

    public ErrorReason getErrorReason() {
        return this.errorCode.getReason();
    }

    public ErrorReason getErrorReasonHttpStatus(){
        return this.errorCode.getReasonHttpStatus();
    }
}