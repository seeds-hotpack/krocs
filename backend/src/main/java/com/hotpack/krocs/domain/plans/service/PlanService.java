package com.hotpack.krocs.domain.plans.service;

import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import jakarta.validation.Valid;

public interface PlanService {
    PlanCreateResponseDTO createPlan(@Valid PlanCreateRequestDTO requestDTO, Long userId, Long subGoalId);
}
