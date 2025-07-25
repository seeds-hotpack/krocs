package com.hotpack.krocs.domain.templates.dto.response;

import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.global.common.entity.Priority;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateResponseDTO {
    private Long templateId;
    private Long userId;
    private String title;
    private Priority priority;
    private Integer duration;
    private String createdAt;
    private String updatedAt;
    private List<SubTemplateResponseDTO> subTemplates;

}
