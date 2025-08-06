package com.hotpack.krocs.domain.plans.service;


import com.hotpack.krocs.domain.plans.dto.request.SubPlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanListResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanResponseDTO;

public interface SubPlanService {

    SubPlanCreateResponseDTO createSubPlans(Long planId,
        SubPlanCreateRequestDTO subPlanCreateRequestDTO);

    SubPlanListResponseDTO getAllSubPlans(Long planId);

    SubPlanResponseDTO getSubPlan(Long planId, Long subPlanId);

    void deleteSubPlan(Long subPlanId);
}

