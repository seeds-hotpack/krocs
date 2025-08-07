package com.hotpack.krocs.domain.plans.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubPlanListResponseDTO {

    @JsonProperty("sub_plans")
    @Schema(description = "SubPlan 리스트")
    private List<SubPlanResponseDTO> subPlans;
}