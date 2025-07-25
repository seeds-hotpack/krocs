package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.UpdateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.GoalRepositoryFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalValidator {

    private final GoalRepositoryFacade goalRepositoryFacade;

    public void validateGoalCreation(CreateGoalRequestDTO requestDTO) {
        validateTitle(requestDTO.getTitle());
        validateDuration(requestDTO.getDuration());
        validateDateRange(requestDTO.getStartDate(), requestDTO.getEndDate());
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
            throw new GoalException(GoalExceptionType.GOAL_TITLE_EMPTY);
        }
        if (title.length() > 200) {
            throw new GoalException(GoalExceptionType.GOAL_TITLE_TOO_LONG);
        }
    }

    public void validateDuration(Integer duration) {
        if (duration == null || duration <= 0) {
            throw new GoalException(GoalExceptionType.GOAL_DURATION_INVALID);
        }
    }

    public void validateDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate != null && endDate != null) {
            if (startDate.isAfter(endDate)) {
                throw new GoalException(GoalExceptionType.INVALID_GOAL_DATE_RANGE);
            }
        }
    }

    public void validateBusinessRules(CreateGoalRequestDTO requestDTO, Long userId) {
        // 중복 제목 검증
        if (goalRepositoryFacade.existsByTitle(requestDTO.getTitle())) {
            throw new GoalException(GoalExceptionType.GOAL_DUPLICATE_TITLE);
        }
    }

    public void validateUpdateDates(UpdateGoalRequestDTO request, Goal existingGoal) {
        LocalDate finalStartDate = request.getStartDate() != null ? request.getStartDate() : existingGoal.getStartDate();
        LocalDate finalEndDate = request.getEndDate() != null ? request.getEndDate() : existingGoal.getEndDate();

        // 최종 날짜 범위 검증
        if (finalStartDate != null && finalEndDate != null) {
            if (finalStartDate.isAfter(finalEndDate)) {
                throw new GoalException(GoalExceptionType.INVALID_GOAL_DATE_RANGE);
            }
        }
    }
}
