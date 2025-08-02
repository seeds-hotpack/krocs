package com.hotpack.krocs.domain.plans.exception;

import com.hotpack.krocs.global.common.response.exception.GeneralException;
import lombok.Getter;

@Getter
public class SubPlanException extends GeneralException {

    private final SubPlanExceptionType subPlanExceptionType;

    public SubPlanException(SubPlanExceptionType subPlanExceptionType) {
        super(subPlanExceptionType);
        this.subPlanExceptionType = subPlanExceptionType;
    }
}

