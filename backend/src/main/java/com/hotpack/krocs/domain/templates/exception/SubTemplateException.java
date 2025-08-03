package com.hotpack.krocs.domain.templates.exception;

import com.hotpack.krocs.global.common.response.exception.GeneralException;
import lombok.Getter;

@Getter
public class SubTemplateException extends GeneralException {

  private final SubTemplateExceptionType subTemplateExceptionType;

  public SubTemplateException(SubTemplateExceptionType subTemplateExceptionType) {
    super(subTemplateExceptionType);
    this.subTemplateExceptionType = subTemplateExceptionType;
  }
}
