package com.hotpack.krocs.domain.plans.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public class PlanListResponseDTO {
    @NotEmpty @Size(min = 1)
    private List<PlanResponseDTO> plans;
}
