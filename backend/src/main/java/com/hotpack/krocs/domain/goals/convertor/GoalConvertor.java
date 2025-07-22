package com.hotpack.krocs.domain.goals.convertor;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GoalConvertor {

    /**
     * CreateGoalRequestDTO를 Goal 엔티티로 변환합니다.
     * 
     * @param requestDTO 생성 요청 DTO
     * @return Goal 엔티티
     */
    public Goal toEntity(CreateGoalRequestDTO requestDTO) {
        return Goal.builder()
                .title(requestDTO.getTitle())
                .priority(requestDTO.getPriority() != null ? requestDTO.getPriority() : Priority.MEDIUM)
                .startDate(requestDTO.getStartDate())
                .endDate(requestDTO.getEndDate())
                .duration(requestDTO.getDuration())
                .isCompleted(false)
                .build();
    }

    /**
     * Goal 엔티티를 CreateGoalResponseDTO로 변환합니다.
     * 
     * @param goal Goal 엔티티
     * @return CreateGoalResponseDTO
     */
    public CreateGoalResponseDTO toCreateResponseDTO(Goal goal) {
        List<SubGoalResponseDTO> subGoalResponseDTOs = goal.getSubGoals() != null ?
                goal.getSubGoals().stream()
                        .map(this::toSubGoalResponseDTO)
                        .collect(Collectors.toList()) :
                List.of();

        int completionPercentage = calculateCompletionPercentage(goal);

        return CreateGoalResponseDTO.builder()
                .goalId(goal.getGoalId())
                .title(goal.getTitle())
                .priority(goal.getPriority())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .duration(goal.getDuration())
                .completed(goal.getIsCompleted())
                .subGoals(subGoalResponseDTOs)
                .completionPercentage(completionPercentage)
                .createdAt(goal.getCreatedAt())
                .updatedAt(goal.getUpdatedAt())
                .build();
    }

    /**
     * SubGoal을 SubGoalResponseDTO로 변환합니다.
     * 
     * @param subGoal SubGoal 엔티티
     * @return SubGoalResponseDTO
     */
    private SubGoalResponseDTO toSubGoalResponseDTO(com.hotpack.krocs.domain.goals.domain.SubGoal subGoal) {
        return SubGoalResponseDTO.builder()
                .subGoalId(subGoal.getSubGoalId())
                .title(subGoal.getTitle())
                .completed(subGoal.getIsCompleted())
                .completionPercentage(subGoal.getIsCompleted() ? 100 : 0)
                .build();
    }

    /**
     * 목표의 완료율을 계산합니다.
     * 
     * @param goal Goal 엔티티
     * @return 완료율 (0-100)
     */
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