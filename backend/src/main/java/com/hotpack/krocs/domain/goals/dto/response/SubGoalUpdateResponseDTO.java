package com.hotpack.krocs.domain.goals.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record SubGoalUpdateResponseDTO(
    @JsonProperty("sub_goal_id")
    Long subGoalId,
    @JsonProperty("goal_id")
    Long goalId,
    String title,
    @JsonProperty("is_completed")
    Boolean isCompleted,
    @JsonProperty("created_at")
    LocalDateTime createdAt,
    @JsonProperty("updated_at")
    LocalDateTime updatedAt
) {

}
