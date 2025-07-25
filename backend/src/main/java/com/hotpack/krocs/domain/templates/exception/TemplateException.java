package com.hotpack.krocs.domain.templates.exception;

import com.hotpack.krocs.global.common.response.exception.GeneralException;
import lombok.Getter;

@Getter
public class TemplateException extends GeneralException  {
    private final TemplateExceptionType templateExceptionType;

    public TemplateException(TemplateExceptionType templateExceptionType){
        super(templateExceptionType);
        this.templateExceptionType = templateExceptionType;
    }
}
