package com.hotpack.krocs.domain.goals.facade;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalRepositoryFacade {

    private final GoalRepository goalRepository;

    @Transactional
    public Goal saveGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public List<Goal> findGoalByDate(LocalDate date) {
        return goalRepository.findByDate(date);
    }

    public List<Goal> findAllGoals() {
        return goalRepository.findAll();
    }

    public Goal findGoalByGoalId(Long goalId) {
        return goalRepository.findGoalByGoalId(goalId);
    }

    @Transactional
    public Goal updateGoal(Goal goal) {
        return goalRepository.save(goal);
    }

    public Goal findById(Long goalId) {
        return goalRepository.findGoalByGoalId(goalId);
    }

    @Transactional
    public void deleteGoal(Long goalId) {
        goalRepository.deleteById(goalId);
    }

    public boolean existsById(Long goalId) {
        return goalRepository.existsById(goalId);
    }

    public boolean existsByTitle(String title) {
        return goalRepository.existsByTitle(title);
    }

    public boolean existsByTitleAndGoalIdNot(String title, Long goalId) {
        return goalRepository.existsByTitleAndGoalIdNot(title, goalId);
    }
}