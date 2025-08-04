package com.hotpack.krocs.domain.plans.converter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanResponseDTO;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanConverter {

    public Plan toEntity(PlanCreateRequestDTO requestDTO, Goal goal, SubGoal subGoal) {
        LocalDateTime startDateTime = requestDTO.getStartDateTime();
        LocalDateTime endDateTime = requestDTO.getEndDateTime();

        if (Boolean.TRUE.equals(requestDTO.getAllDay())) {
            if (startDateTime != null) {
                startDateTime = startDateTime.toLocalDate().atStartOfDay();
            }

            if (endDateTime != null) {
                endDateTime = endDateTime.toLocalDate().atTime(23, 59, 59);
            }
        }

        return Plan.builder()
                .goal(goal)
                .subGoal(subGoal)
                .title(requestDTO.getTitle())
                .startDateTime(startDateTime)
                .endDateTime(endDateTime)
                .allDay(requestDTO.getAllDay())
                .isCompleted(false)
                .build();
    }

    public PlanResponseDTO toEntity(Plan plan) {
        PlanResponseDTO.PlanResponseDTOBuilder builder = PlanResponseDTO.builder()
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