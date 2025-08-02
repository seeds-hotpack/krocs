package com.hotpack.krocs.domain.goals.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hotpack.krocs.global.common.entity.Priority;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GoalResponseDTO {

  private final Long goalId;

  private final String title;

  private Priority priority;

  private LocalDate startDate;

  private LocalDate endDate;

  private Boolean isCompleted;

  private final List<SubGoalResponseDTO> subGoals;

  private final Integer completionPercentage;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private final LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
  private final LocalDateTime updatedAt;
}