package com.hotpack.krocs.domain.plans.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubPlanResponseDTO {

    private final Long subPlanId;

    private final String title;

    private final Boolean isCompleted;
}
