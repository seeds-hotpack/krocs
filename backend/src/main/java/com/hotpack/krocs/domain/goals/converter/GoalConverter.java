package com.hotpack.krocs.domain.goals.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.GoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoalConverter {

  private final SubGoalConverter subGoalConverter;
  
  public Goal toGoalEntity(GoalCreateRequestDTO requestDTO) {
    return Goal.builder()
        .title(requestDTO.getTitle())
        .priority(requestDTO.getPriority() != null ? requestDTO.getPriority() : Priority.MEDIUM)
        .startDate(requestDTO.getStartDate())
        .endDate(requestDTO.getEndDate())
        .duration(requestDTO.getDuration())
        .isCompleted(false)
        .build();
  }

  public CreateGoalResponseDTO toCreateGoalResponseDTO(Goal goal) {
    List<SubGoalResponseDTO> subGoalResponseDTOs = goal.getSubGoals() != null ?
        goal.getSubGoals().stream()
            .map(subGoalConverter::toSubGoalResponseDTO)
            .collect(Collectors.toList()) :
        List.of();

    return CreateGoalResponseDTO.builder()
        .goalId(goal.getGoalId())
        .title(goal.getTitle())
        .priority(goal.getPriority())
        .startDate(goal.getStartDate())
        .endDate(goal.getEndDate())
        .duration(goal.getDuration())
        .completed(goal.getIsCompleted())
        .subGoals(subGoalResponseDTOs)
        .createdAt(goal.getCreatedAt())
        .updatedAt(goal.getUpdatedAt())
        .build();
  }
} 