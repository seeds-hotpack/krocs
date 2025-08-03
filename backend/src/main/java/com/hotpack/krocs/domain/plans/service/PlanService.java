package com.hotpack.krocs.domain.plans.service;

import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanListResponseDTO;
import jakarta.validation.Valid;

public interface PlanService {
    PlanCreateResponseDTO createPlan(@Valid PlanCreateRequestDTO requestDTO, Long userId, Long goalId);

    PlanListResponseDTO getAllPlans(Long userId);

    PlanListResponseDTO getPlanById(Long planId, Long userId);
}
