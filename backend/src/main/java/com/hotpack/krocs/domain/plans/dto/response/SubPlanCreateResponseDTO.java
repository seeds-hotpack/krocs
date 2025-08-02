package com.hotpack.krocs.domain.plans.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalResponseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SubPlanCreateResponseDTO {

    @JsonProperty("plan_id")
    @NotBlank
    private Long planId;

    @JsonProperty("created_sub_plans")
    @NotBlank
    private List<SubPlanResponseDTO> createdSubPlans;
}