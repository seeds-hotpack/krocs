package com.hotpack.krocs.domain.plans.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubPlanCreateResponseDTO {

    @JsonProperty("plan_id")
    @NotBlank
    private Long planId;

    @JsonProperty("created_sub_plans")
    @NotEmpty
    private List<SubPlanResponseDTO> createdSubPlans;
}