package com.hotpack.krocs.domain.plans.repository;

import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.domain.SubPlan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubPlanRepository extends JpaRepository<SubPlan, Long> {

    List<SubPlan> findSubPlansByPlan(Plan plan);

    SubPlan findSubPlansBySubPlanId(Long subPlanId);

    void deleteSubPlanBySubPlanId(Long subPlanId);
}
