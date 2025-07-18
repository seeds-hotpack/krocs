package com.hotpack.krocs.common.exception;

import com.hotpack.krocs.common.dto.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public CommonResponse<Void> handleBusinessException(BusinessException e) {
    return CommonResponse.fail(e.getErrorCode());
  }

  @ExceptionHandler(Exception.class)
  public CommonResponse<Void> handleOtherExceptions(Exception e) {
    return CommonResponse.fail(ErrorCode.INTERNAL_SERVER_ERROR);
  }
}