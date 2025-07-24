package com.hotpack.krocs.domain.templates.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubTemplateResponseDTO {

    private final Long subTemplateId;

    private final String title;
}
