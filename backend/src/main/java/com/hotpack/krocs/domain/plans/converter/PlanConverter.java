package com.hotpack.krocs.domain.plans.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanConverter {

    public Plan toEntity(PlanCreateRequestDTO requestDTO, Goal goal, SubGoal subGoal) {
        return Plan.builder()
                .goal(goal)
                .subGoal(subGoal)
                .title(requestDTO.getTitle())
                .startDateTime(requestDTO.getStartDateTime())
                .endDateTime(requestDTO.getEndDateTime())
                .allDay(requestDTO.getAllDay())
                .isCompleted(false)
                .build();
    }

    public PlanCreateResponseDTO toCreateResponseDTO(Plan plan) {
        PlanCreateResponseDTO.PlanCreateResponseDTOBuilder builder = PlanCreateResponseDTO.builder()
            .planId(plan.getPlanId())
            .title(plan.getTitle())
            .startDateTime(plan.getStartDateTime())
            .endDateTime(plan.getEndDateTime())
            .allDay(plan.getAllDay())
            .isCompleted(plan.getIsCompleted())
            .completedAt(plan.getCompletedAt())
            .createdAt(plan.getCreatedAt())
            .updatedAt(plan.getUpdatedAt());

        if (plan.getGoal() != null) {
            builder.goalId(plan.getGoal().getGoalId());
        }

        if (plan.getSubGoal() != null) {
            builder.subGoalId(plan.getSubGoal().getSubGoalId());
        }

        return builder.build();
    }
}