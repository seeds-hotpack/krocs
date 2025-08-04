package com.hotpack.krocs.domain.plans.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PlanListResponseDTO {
    @NotEmpty @Size(min = 1)
    private List<PlanResponseDTO> plans;
}
