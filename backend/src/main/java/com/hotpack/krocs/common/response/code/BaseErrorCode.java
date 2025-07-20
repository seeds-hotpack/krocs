package com.hotpack.krocs.common.response.code;

public interface BaseErrorCode {
    public Reason getReason();

    public Reason getReasonHttpStatus();
}