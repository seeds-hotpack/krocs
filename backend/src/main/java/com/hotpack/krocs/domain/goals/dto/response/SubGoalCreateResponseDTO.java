package com.hotpack.krocs.domain.goals.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Builder;

@Builder
public record SubGoalCreateResponseDTO(
    @JsonProperty("goal_id")
    @NotBlank
    Long goalId,
    @JsonProperty("created_sub_goals")
    @NotBlank
    List<SubGoalResponseDTO> createdSubGoals
) {

}
