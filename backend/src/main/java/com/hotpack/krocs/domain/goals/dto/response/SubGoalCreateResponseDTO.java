package com.hotpack.krocs.domain.goals.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubGoalCreateResponseDTO {

  @JsonProperty("goal_id")
  @NotBlank
  private Long goalId;

  @JsonProperty("created_sub_goals")
  @NotBlank
  private List<SubGoalResponseDTO> createdSubGoals;
}
