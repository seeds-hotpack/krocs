package com.hotpack.krocs.domain.plans.service;

import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanListResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanResponseDTO;

public interface PlanService {
    PlanResponseDTO createPlan(PlanCreateRequestDTO requestDTO, Long userId, Long goalId);

    PlanListResponseDTO getAllPlans(Long userId);

    PlanResponseDTO getPlanById(Long planId, Long userId);
}
