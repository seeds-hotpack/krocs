package com.hotpack.krocs.domain.plans.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SubPlanRequestDTO {

    @NotBlank
    @Schema(description = "SubPlan 제목", example = "에프킬라 사기")
    private String title;
}

