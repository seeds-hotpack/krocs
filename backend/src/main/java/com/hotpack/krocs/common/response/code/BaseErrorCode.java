package com.hotpack.krocs.common.response.code;

public interface BaseErrorCode {
    public ErrorReason getReason();

    public ErrorReason getReasonHttpStatus();
}