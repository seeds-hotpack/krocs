package com.hotpack.krocs.global.common.response.exception.handler;

import com.hotpack.krocs.global.common.response.code.BaseCode;
import com.hotpack.krocs.global.common.response.exception.GeneralException;

public class GoalHandler extends GeneralException {

    public GoalHandler(BaseCode errorCode) {
        super(errorCode);
    }
}