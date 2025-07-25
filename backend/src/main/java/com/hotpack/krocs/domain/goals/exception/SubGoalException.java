package com.hotpack.krocs.domain.goals.exception;

import com.hotpack.krocs.global.common.response.exception.GeneralException;
import lombok.Getter;

@Getter
public class SubGoalException extends GeneralException {

  private final SubGoalExceptionType subGoalExceptionType;

  public SubGoalException(SubGoalExceptionType subGoalExceptionType) {
    super(subGoalExceptionType);
    this.subGoalExceptionType = subGoalExceptionType;
  }
}
