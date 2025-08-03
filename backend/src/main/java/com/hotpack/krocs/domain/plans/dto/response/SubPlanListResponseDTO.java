package com.hotpack.krocs.domain.plans.dto.response;



import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubPlanListResponseDTO {
    private List<SubPlanResponseDTO> subPlans;

}
