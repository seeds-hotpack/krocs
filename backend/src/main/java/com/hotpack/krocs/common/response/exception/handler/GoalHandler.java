package com.hotpack.krocs.common.response.exception.handler;

import com.hotpack.krocs.common.response.code.BaseErrorCode;
import com.hotpack.krocs.common.response.exception.GeneralException;

public class GoalHandler extends GeneralException {

    public GoalHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}