package com.hotpack.krocs.domain.goals.facade;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Goal Repository Facade
 * 데이터 접근 계층을 추상화합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalRepositoryFacade {

    private final GoalRepository goalRepository;

    /**
     * 대목표를 저장합니다.
     * 
     * @param goal 저장할 대목표
     * @return 저장된 대목표
     */
    @Transactional
    public Goal saveGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public List<Goal> findGoalByDate(LocalDateTime dateTime) {
        return goalRepository.findByDateTime(dateTime.toLocalDate());
    }

    public List<Goal> findAllGoals() {
        return goalRepository.findAll();
    }

    public Goal findGoalByGoalId(Long goalId) {
        return goalRepository.findGoalByGoalId(goalId);
    }
}