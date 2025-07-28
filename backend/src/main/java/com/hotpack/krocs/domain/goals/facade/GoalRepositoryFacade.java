package com.hotpack.krocs.domain.goals.facade;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.exception.SubGoalException;
import com.hotpack.krocs.domain.goals.exception.SubGoalExceptionType;
import com.hotpack.krocs.domain.goals.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

  public Goal findGoalById(Long id) {
    return goalRepository.findById(id)
        .orElseThrow(() -> new SubGoalException(SubGoalExceptionType.SUB_GOAL_GOAL_NOT_FOUND));
  }
} 