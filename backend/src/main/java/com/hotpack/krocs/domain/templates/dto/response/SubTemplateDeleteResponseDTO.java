package com.hotpack.krocs.domain.templates.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubTemplateDeleteResponseDTO {

    @JsonProperty("sub_template_id")
    private final Long subTemplateId;

}
