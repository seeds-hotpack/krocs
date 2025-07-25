package com.hotpack.krocs.domain.templates.validator;

import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.exception.TemplateException;
import com.hotpack.krocs.domain.templates.exception.TemplateExceptionType;
import com.hotpack.krocs.global.common.entity.Priority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class TemplateValidator {

    public boolean isTitleEmpty(String title) {
        return !StringUtils.hasText(title); // null, "", "   "
    }

    public boolean isTitleTooLong(String title) {
        return title != null && title.length() > 200;
    }

    public boolean isValidDuration(Integer duration) {
        return duration != null && duration > 0;
    }

    public boolean isValidPriority(String priorityCheck){
        try {
            Priority priority = Priority.valueOf(priorityCheck.toUpperCase()); // 대소문자 처리도 고려
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public TemplateValidationResult validateTemplateCreation(CreateTemplateRequestDTO dto) {
        return new TemplateValidationResult(
                isTitleEmpty(dto.getTitle()),
                isTitleTooLong(dto.getTitle()),
                isValidDuration(dto.getDuration())
        );
    }

    public record TemplateValidationResult(
            boolean validTitleEmpty,
            boolean validTitleTooLong,
            boolean validDuration
    ) {
        public boolean isAllValid() {
            return validTitleEmpty && validTitleTooLong && validDuration;
        }
    }
}

