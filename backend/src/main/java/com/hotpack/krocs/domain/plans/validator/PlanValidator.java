package com.hotpack.krocs.domain.plans.validator;

import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlanValidator {

    public void validatePlanCreation(PlanCreateRequestDTO requestDTO, Long goalId) {
        validateTitle(requestDTO.getTitle());
        validateGoalIdParameter(goalId);
        validateDateTime(requestDTO);
        validateEnergy(requestDTO.getEnergy());
    }

    public void validateGoalIdParameter(Long goalId) {
        if (goalId == null) {
            throw new GoalException(GoalExceptionType.GOAL_INVALID_GOAL_ID);
        }
        if (goalId <= 0) {
            throw new GoalException(GoalExceptionType.GOAL_INVALID_GOAL_ID);
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

        if (!allDay) {
            if (requestDTO.getStartDateTime() == null) {
                throw new PlanException(PlanExceptionType.PLAN_START_TIME_REQUIRED);
            }
            if (requestDTO.getEndDateTime() == null) {
                throw new PlanException(PlanExceptionType.PLAN_END_TIME_REQUIRED);
            }

            if (requestDTO.getStartDateTime().isAfter(requestDTO.getEndDateTime()) ||
                    requestDTO.getStartDateTime().equals(requestDTO.getEndDateTime())) {
                throw new PlanException(PlanExceptionType.INVALID_PLAN_DATE_RANGE);
            }
        }
    }

    public void validateEnergy(Integer energy) {
        if (energy != null && (energy < 1 || energy > 10)) {
            throw new PlanException(PlanExceptionType.PLAN_INVALID_ENERGY);
        }
    }
}
