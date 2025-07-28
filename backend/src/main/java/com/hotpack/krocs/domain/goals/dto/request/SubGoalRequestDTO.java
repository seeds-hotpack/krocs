package com.hotpack.krocs.domain.goals.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SubGoalRequestDTO {

  @NotBlank
  @Schema(description = "SubGoal 제목", example = "Liquibase 이슈 해결")
  private String title;
}
