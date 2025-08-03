package com.hotpack.krocs.domain.plans.service;


import com.hotpack.krocs.domain.goals.dto.response.SubGoalListResponseDTO;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanListResponseDTO;
import org.springframework.stereotype.Service;

public interface SubPlanService {

    SubPlanCreateResponseDTO createSubPlans(Long planId,
        SubPlanCreateRequestDTO subPlanCreateRequestDTO);

    SubPlanListResponseDTO getAllSubPlans(Long planId);
}

