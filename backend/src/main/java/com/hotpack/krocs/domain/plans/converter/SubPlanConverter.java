package com.hotpack.krocs.domain.plans.converter;

import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.domain.SubPlan;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanUpdateResponseDTO;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubPlanConverter {

    public static SubPlanUpdateResponseDTO toSubPlanUpdateResponseDTO(SubPlan subPlan) {
        return SubPlanUpdateResponseDTO.builder()
            .subPlanId(subPlan.getSubPlanId())
            .planId(subPlan.getPlan().getPlanId())
            .title(subPlan.getTitle())
            .isCompleted(subPlan.getIsCompleted())
            .completedAt(subPlan.getCompletedAt())
            .createdAt(subPlan.getCreatedAt())
            .updatedAt(subPlan.getUpdatedAt())
            .build();
    }

    public SubPlan toEntity(Plan plan, SubPlanRequestDTO requestDTO) {
        return SubPlan.builder()
            .plan(plan)
            .title(requestDTO.getTitle())
            .build();
    }

    public List<SubPlan> toSubPlanEntityList(Plan plan,
        SubPlanCreateRequestDTO subPlanCreateRequestDTO) {
        List<SubPlan> subPlans = new ArrayList<>();
        for (SubPlanRequestDTO subPlanRequestDTO : subPlanCreateRequestDTO.getSubPlans()) {
            subPlans.add(toEntity(plan, subPlanRequestDTO));
        }
        return subPlans;
    }

    public SubPlanResponseDTO toSubPlanResponseDTO(SubPlan subPlan) {
        return SubPlanResponseDTO.builder()
            .subPlanId(subPlan.getSubPlanId())
            .title(subPlan.getTitle())
            .isCompleted(subPlan.getIsCompleted())
            .completedAt(subPlan.getCompletedAt())
            .createdAt(subPlan.getCreatedAt())
            .updatedAt(subPlan.getUpdatedAt())
            .build();
    }

    public List<SubPlanResponseDTO> toSubPlanResponseListDTO(List<SubPlan> subPlans) {
        List<SubPlanResponseDTO> subPlanResponseDTOs = new ArrayList<>();
        for (SubPlan subPlan : subPlans) {
            subPlanResponseDTOs.add(toSubPlanResponseDTO(subPlan));
        }
        return subPlanResponseDTOs;
    }
}