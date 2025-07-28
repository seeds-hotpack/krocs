package com.hotpack.krocs.domain.templates.dto.request;

import com.hotpack.krocs.global.common.entity.Priority;
import lombok.*;

@Getter
@Builder
public class TemplateUpdateRequestDTO {
    private String title;
    private Priority priority;
    private Integer duration;
}
