package com.hotpack.krocs.domain.templates.validator;

import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


@Component
public class TemplateValidator {

    public boolean isValidTitle(String title) {
        return StringUtils.hasText(title) && title.length() <= 200;
    }

    public boolean isValidDuration(Integer duration) {
        return duration != null && duration > 0;
    }

    public TemplateValidationResult validateTemplateCreation(CreateTemplateRequestDTO dto) {
        return new TemplateValidationResult(
                isValidTitle(dto.getTitle()),
                isValidDuration(dto.getDuration())
        );
    }

    public record TemplateValidationResult(
            boolean validTitle,
            boolean validDuration
    ) {
        public boolean isAllValid() {
            return validTitle && validDuration;
        }
    }
}

