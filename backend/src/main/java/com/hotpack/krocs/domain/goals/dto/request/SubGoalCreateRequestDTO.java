package com.hotpack.krocs.domain.goals.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Builder;

@Builder
public record SubGoalCreateRequestDTO(
    @JsonProperty("sub_goals")
    @Schema(description = "SubGoal 리스트")
    List<SubGoalRequestDTO> subGoals
) {

}
