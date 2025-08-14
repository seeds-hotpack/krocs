package com.hotpack.krocs.domain.plans.facade;

import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.exception.SubPlanException;
import com.hotpack.krocs.domain.plans.exception.SubPlanExceptionType;
import com.hotpack.krocs.domain.plans.repository.PlanRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanRepositoryFacade {
    private final PlanRepository planRepository;

    @Transactional
    public Plan savePlan(Plan plan) {
        return planRepository.save(plan);
    }

    public Plan findPlanById(Long id) {
        return planRepository.findById(id)
            .orElseThrow(() -> new SubPlanException(SubPlanExceptionType.SUB_PLAN_PLAN_NOT_FOUND));
    }

    public List<Plan> findPlans(LocalDateTime dateTime) {
        return planRepository.findPlans(dateTime);
    }

    @Transactional
    public void deletePlanByPlanId(Long planId) {
        planRepository.deleteById(planId);
    }
}
