package com.hotpack.krocs.domain.templates.dto.request;

import com.hotpack.krocs.global.common.entity.Priority;
import lombok.Builder;


@Builder
public record UpdateTemplateRequestDTO(
        String title,
        String priority,
        Integer duration
) {}
