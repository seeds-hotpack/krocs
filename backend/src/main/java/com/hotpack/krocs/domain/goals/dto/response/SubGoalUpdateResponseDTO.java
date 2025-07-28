package com.hotpack.krocs.domain.goals.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubGoalUpdateResponseDTO {

  @JsonProperty("sub_goal_id")
  private Long subGoalId;

  @JsonProperty("goal_id")
  private Long goalId;

  private String title;
  @JsonProperty("is_completed")
  private Boolean isCompleted;

  @JsonProperty("created_at")
  private LocalDateTime createdAt;

  @JsonProperty("updated_at")
  private LocalDateTime updatedAt;
}
