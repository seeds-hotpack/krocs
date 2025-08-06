package com.hotpack.krocs.domain.plans.facade;

import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.domain.SubPlan;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanResponseDTO;
import com.hotpack.krocs.domain.plans.exception.SubPlanException;
import com.hotpack.krocs.domain.plans.exception.SubPlanExceptionType;
import com.hotpack.krocs.domain.plans.repository.SubPlanRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubPlanRepositoryFacade {

    private final SubPlanRepository subPlanRepository;

    @Transactional
    public List<SubPlan> saveSubPlans(List<SubPlan> subPlans) {
        return subPlanRepository.saveAll(subPlans);
    }

    // 빈 리스트는 정상 응답으로 간주하고 그대로 반환
    public List<SubPlan> findSubPlansByPlan(Plan plan) {
        List<SubPlan> subPlans = subPlanRepository.findSubPlansByPlan(plan);
        return subPlans;
    }

    public SubPlan findSubPlanBySubPlanId(Long subPlanId) {
        SubPlan subPlan = subPlanRepository.findSubPlansBySubPlanId(subPlanId);
        if (subPlan == null) {
            throw new SubPlanException(SubPlanExceptionType.SUB_PLAN_NOT_FOUND);
        }
        return subPlan;
    }

    public void deleteSubPlanBySubPlanId(Long subPlanId) {
        findSubPlanBySubPlanId(subPlanId);
        subPlanRepository.deleteSubPlanBySubPlanId(subPlanId);
    }


    @Transactional
    public SubPlan saveSubPlan(SubPlan subPlan) {
        return subPlanRepository.save(subPlan);
    }

}