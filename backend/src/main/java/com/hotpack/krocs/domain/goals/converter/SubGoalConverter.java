package com.hotpack.krocs.domain.goals.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalUpdateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalUpdateResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubGoalConverter {

  public SubGoal toSubGoalEntity(Goal goal, SubGoalRequestDTO requestDTO) {
    return SubGoal.builder()
        .goal(goal)
        .title(requestDTO.getTitle())
        .build();
  }
  
  public SubGoal toSubGoalEntity(SubGoal subGoal, SubGoalUpdateRequestDTO requestDTO) {
    String title = subGoal.getTitle();
    if (requestDTO.getTitle() != null) {
      title = requestDTO.getTitle();
    }

    boolean isCompleted = subGoal.getIsCompleted();
    if (requestDTO.getIsCompleted() != null) {
      isCompleted = requestDTO.getIsCompleted();
    }

    return SubGoal.builder()
        .subGoalId(subGoal.getSubGoalId())
        .goal(subGoal.getGoal())
        .title(title)
        .isCompleted(isCompleted)
        .build();
  }

  public List<SubGoal> toSubGoalEntityList(Goal goal,
      SubGoalCreateRequestDTO subGoalCreateRequestDTO) {
    List<SubGoal> subGoals = new ArrayList<>();
    for (SubGoalRequestDTO subGoalRequestDTO : subGoalCreateRequestDTO.getSubGoals()) {
      subGoals.add(toSubGoalEntity(goal, subGoalRequestDTO));
    }
    return subGoals;
  }

  public SubGoalResponseDTO toSubGoalResponseDTO(SubGoal subGoal) {
    return SubGoalResponseDTO.builder()
        .subGoalId(subGoal.getSubGoalId())
        .title(subGoal.getTitle())
        .completed(subGoal.getIsCompleted())
        .completionPercentage(subGoal.getIsCompleted() ? 100 : 0)
        .build();
  }

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

  public List<SubGoalResponseDTO> toSubGoalResponseListDTO(List<SubGoal> subGoals) {
    List<SubGoalResponseDTO> subGoalResponseDTOs = new ArrayList<>();
    for (SubGoal subGoal : subGoals) {
      subGoalResponseDTOs.add(toSubGoalResponseDTO(subGoal));
    }
    return subGoalResponseDTOs;
  }
}
