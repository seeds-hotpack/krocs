package com.hotpack.krocs.domain.goals.convertor;

import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalUpdateResponseDTO;

public class SubGoalConvertor {


  public static SubGoalUpdateResponseDTO toSubGoalUpdateResponseDTO(SubGoal subGoal) {
    return SubGoalUpdateResponseDTO.builder()
        .subGoalId(subGoal.getSubGoalId())
        .goalId(subGoal.getGoal().getGoalId())
        .title(subGoal.getTitle())
        .isCompleted(subGoal.getIsCompleted())
        .createdAt(subGoal.getCreatedAt())
        .updatedAt(subGoal.getUpdatedAt())
        .build();
  }

}
