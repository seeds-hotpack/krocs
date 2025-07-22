package com.hotpack.krocs.common.response.exception.handler;

import com.hotpack.krocs.common.response.code.BaseCode;
import com.hotpack.krocs.common.response.exception.GeneralException;

public class GoalHandler extends GeneralException {

    public GoalHandler(BaseCode errorCode) {
        super(errorCode);
    }
}