package com.hotpack.krocs.domain.templates.dto.request;

import java.time.LocalDateTime;

public record UpdateTemplateRequestDTO(
        String title,
        String priority,
        Integer duration
) {}
