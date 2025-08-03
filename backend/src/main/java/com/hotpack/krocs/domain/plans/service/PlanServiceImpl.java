package com.hotpack.krocs.domain.plans.service;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.GoalRepositoryFacade;
import com.hotpack.krocs.domain.goals.facade.SubGoalRepositoryFacade;
import com.hotpack.krocs.domain.plans.converter.PlanConverter;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import com.hotpack.krocs.domain.plans.facade.PlanRepositoryFacade;
import com.hotpack.krocs.domain.plans.validator.PlanValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanServiceImpl implements PlanService{

    private final PlanRepositoryFacade planRepositoryFacade;
    private final PlanConverter planConverter;
    private final SubGoalRepositoryFacade subGoalRepositoryFacade;
    private final PlanValidator planValidator;

    @Override
    @Transactional
    public PlanCreateResponseDTO createPlan(PlanCreateRequestDTO requestDTO, Long userId, Long subGoalId) {
        try {
            planValidator.validatePlanCreation(requestDTO, subGoalId);

            SubGoal subGoal = null;
            Goal goal = null;

            if (subGoalId != null) {
                subGoal = subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId);
                if (subGoal == null) {
                    throw new PlanException(PlanExceptionType.PLAN_SUB_GOAL_NOT_FOUND);
                }

                goal = subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId);
                if (goal == null) {
                    throw new PlanException(PlanExceptionType.PLAN_GOAL_NOT_FOUND);
                }
            }

            Plan plan = planConverter.toEntity(requestDTO, goal, subGoal);
            Plan savedPlan = planRepositoryFacade.savePlan(plan);

            return planConverter.toCreateResponseDTO(savedPlan);
        } catch (PlanException e) {
            throw e;
        } catch (Exception e) {
            log.error("일정 생성 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new PlanException(PlanExceptionType.PLAN_CREATION_FAILED);
        }
    }
}
