package com.hotpack.krocs.domain.plans.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlanConverter {

    public Plan toEntity(PlanCreateRequestDTO requestDTO, Goal goal) {
        return Plan.builder()
                .goal(goal)
                .title(requestDTO.getTitle())
                .startDateTime(requestDTO.getStartDateTime())
                .endDateTime(requestDTO.getEndDateTime())
                .allDay(requestDTO.getAllDay())
                .isCompleted(false)
                .energy(requestDTO.getEnergy())
                .build();
    }

    public PlanCreateResponseDTO toCreateResponseDTO(Plan plan, Long goalId) {
        return PlanCreateResponseDTO.builder()
                .planId(plan.getPlanId())
                .goalId(goalId)
                .title(plan.getTitle())
                .startDateTime(plan.getStartDateTime())
                .endDateTime(plan.getEndDateTime())
                .allDay(plan.getAllDay())
                .isCompleted(plan.getIsCompleted())
                .energy(plan.getEnergy())
                .completedAt(plan.getCompletedAt())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .build();
    }
}