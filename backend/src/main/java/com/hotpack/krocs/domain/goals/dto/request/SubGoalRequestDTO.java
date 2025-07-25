package com.hotpack.krocs.domain.goals.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record SubGoalRequestDTO(
    @NotBlank
    @Schema(description = "SubGoal 제목", example = "Liquibase 이슈 해결")
    String title
) {

}
