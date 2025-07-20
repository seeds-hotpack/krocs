package com.hotpack.krocs.common.response.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class Reason {

    private HttpStatus httpStatus;

    private final boolean isSuccess;
    private final String code;
    private final String message;
    private final Object data; // 추가 데이터 필드

    public boolean getIsSuccess(){return isSuccess;}
}