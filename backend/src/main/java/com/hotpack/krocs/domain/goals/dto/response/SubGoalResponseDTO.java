package com.hotpack.krocs.domain.goals.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubGoalResponseDTO {

  private final Long subGoalId;

  private final String title;

  private final Boolean isCompleted;
}