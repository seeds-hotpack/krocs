package com.hotpack.krocs.domain.plans.exception;

import com.hotpack.krocs.global.common.response.exception.GeneralException;
import lombok.Getter;

@Getter
public class PlanException extends GeneralException {

    private final PlanExceptionType planExceptionType;

    public PlanException(PlanExceptionType planExceptionType) {
        super(planExceptionType);
        this.planExceptionType = planExceptionType;
    }
} 