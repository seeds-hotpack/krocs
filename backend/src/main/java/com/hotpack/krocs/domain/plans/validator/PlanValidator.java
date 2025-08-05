package com.hotpack.krocs.domain.plans.validator;

import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanValidator {

    public void validatePlanCreation(PlanCreateRequestDTO requestDTO, Long subGoalId) {
        validateTitle(requestDTO.getTitle());
        validateSubGoalIdParameter(subGoalId);
        validateDateTime(requestDTO);
    }

    public void validateGetPlan(Long planId) {
        validatePlanIdParameter(planId);
    }

    private void validatePlanIdParameter(Long planId) {
        if (planId == null || planId <= 0) {
            throw new PlanException(PlanExceptionType.PLAN_INVALID_PLAN_ID);
        }
    }

    public void validateSubGoalIdParameter(Long subGoalId) {
        if (subGoalId == null) return;
        if (subGoalId <= 0) {
            throw new PlanException(PlanExceptionType.GOAL_INVALID_GOAL_ID);
        }
    }

    public void validateTitle(String title) {
        if (!StringUtils.hasText(title)) {
            throw new PlanException(PlanExceptionType.PLAN_TITLE_EMPTY);
        }
        if (title.length() > 200) {
            throw new PlanException(PlanExceptionType.PLAN_TITLE_TOO_LONG);
        }
    }

    public void validateDateTime(PlanCreateRequestDTO requestDTO) {
        Boolean allDay = requestDTO.getAllDay();

        if (allDay == null) {
            allDay = false;
        }

        if (requestDTO.getStartDateTime() == null) {
            throw new PlanException(PlanExceptionType.PLAN_START_TIME_REQUIRED);
        }

        if (requestDTO.getEndDateTime() == null) {
            throw new PlanException(PlanExceptionType.PLAN_END_TIME_REQUIRED);
        }

        if (allDay) {
            LocalDate startDate = requestDTO.getStartDateTime().toLocalDate();
            LocalDate endDate = requestDTO.getEndDateTime().toLocalDate();

            if (startDate.isAfter(endDate)) {
                throw new PlanException(PlanExceptionType.INVALID_PLAN_DATE_RANGE);
            }
        } else {
            if (requestDTO.getStartDateTime().isAfter(requestDTO.getEndDateTime())) {
                throw new PlanException(PlanExceptionType.INVALID_PLAN_DATE_RANGE);
            }
        }
    }
}
