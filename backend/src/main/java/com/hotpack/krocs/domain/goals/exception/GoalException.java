package com.hotpack.krocs.domain.goals.exception;

import com.hotpack.krocs.global.common.response.exception.GeneralException;
import lombok.Getter;

@Getter
public class GoalException extends GeneralException {

    private final GoalExceptionType goalExceptionType;

    public GoalException(GoalExceptionType goalExceptionType) {
        super(goalExceptionType);
        this.goalExceptionType = goalExceptionType;
    }
} 