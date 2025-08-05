package com.hotpack.krocs.domain.plans.service;


import com.hotpack.krocs.domain.goals.dto.response.SubGoalListResponseDTO;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanUpdateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanListResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanUpdateResponseDTO;
import org.springframework.stereotype.Service;

public interface SubPlanService {

    SubPlanCreateResponseDTO createSubPlans(Long planId,
        SubPlanCreateRequestDTO subPlanCreateRequestDTO);

    SubPlanListResponseDTO getAllSubPlans(Long planId);

    SubPlanResponseDTO getSubPlan(Long planId, Long subPlanId);

    SubPlanUpdateResponseDTO updateSubPlan(Long subPlanId, SubPlanUpdateRequestDTO requestDTO);

    void deleteSubPlan(Long subPlanId);
}

