package com.hotpack.krocs.domain.templates.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotpack.krocs.global.common.entity.Priority;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class TemplateCreateResponseDTO {

    private final Long templateId;

    private final String title;

    private final Priority priority;

    private final Integer duration;

    private final List<SubTemplateResponseDTO> subTemplates;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime updatedAt;
}