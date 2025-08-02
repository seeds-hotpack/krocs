package com.hotpack.krocs.domain.goals.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.dto.request.GoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalCreateResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoalConverter {

  public Goal toEntity(GoalCreateRequestDTO requestDTO) {
    return Goal.builder()
        .title(requestDTO.getTitle())
        .priority(requestDTO.getPriority() != null ? requestDTO.getPriority() : Priority.MEDIUM)
        .startDate(requestDTO.getStartDate())
        .endDate(requestDTO.getEndDate())
        .isCompleted(false)
        .build();
  }

  public GoalCreateResponseDTO toCreateResponseDTO(Goal goal) {
    List<SubGoalResponseDTO> subGoalResponseDTOs = goal.getSubGoals() != null ?
        goal.getSubGoals().stream()
            .map(this::toSubGoalResponseDTO)
            .collect(Collectors.toList()) :
        List.of();

    return GoalCreateResponseDTO.builder()
        .goalId(goal.getGoalId())
        .title(goal.getTitle())
        .priority(goal.getPriority())
        .startDate(goal.getStartDate())
        .endDate(goal.getEndDate())
        .isCompleted(goal.getIsCompleted())
        .subGoals(subGoalResponseDTOs)
        .createdAt(goal.getCreatedAt())
        .updatedAt(goal.getUpdatedAt())
        .build();
  }

  private SubGoalResponseDTO toSubGoalResponseDTO(
      SubGoal subGoal) {
    return SubGoalResponseDTO.builder()
        .subGoalId(subGoal.getSubGoalId())
        .title(subGoal.getTitle())
        .isCompleted(subGoal.getIsCompleted())
        .build();
  }

  public List<GoalResponseDTO> toGoalResponseDTO(List<Goal> goals) {
    return goals.stream()
        .map(this::toGoalResponseDTO)
        .collect(Collectors.toList());
  }

  public GoalResponseDTO toGoalResponseDTO(Goal goal) {
    List<SubGoalResponseDTO> subGoalResponseDTOs = goal.getSubGoals() != null ?
        goal.getSubGoals().stream()
            .map(this::toSubGoalResponseDTO)
            .collect(Collectors.toList()) :
        List.of();

    int completionPercentage = calculateCompletionPercentage(goal);

    return GoalResponseDTO.builder()
        .goalId(goal.getGoalId())
        .title(goal.getTitle())
        .priority(goal.getPriority())
        .startDate(goal.getStartDate())
        .endDate(goal.getEndDate())
        .isCompleted(goal.getIsCompleted())
        .subGoals(subGoalResponseDTOs)
        .completionPercentage(completionPercentage)
        .createdAt(goal.getCreatedAt())
        .updatedAt(goal.getUpdatedAt())
        .build();
  }

  private int calculateCompletionPercentage(Goal goal) {
    if (goal.getSubGoals() == null || goal.getSubGoals().isEmpty()) {
      return goal.getIsCompleted() ? 100 : 0;
    }

    long completedCount = goal.getSubGoals().stream()
        .filter(com.hotpack.krocs.domain.goals.domain.SubGoal::getIsCompleted)
        .count();

    return (int) ((completedCount * 100) / goal.getSubGoals().size());
  }
}