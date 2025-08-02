package com.hotpack.krocs.domain.plans.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SubPlanCreateRequestDTO {

    @JsonProperty("sub_plans")
    @Schema(description = "SubPlan 리스트")
    private List<SubPlanRequestDTO> subPlans;
}