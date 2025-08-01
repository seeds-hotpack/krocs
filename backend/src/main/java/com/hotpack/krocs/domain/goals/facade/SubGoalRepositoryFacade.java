package com.hotpack.krocs.domain.goals.facade;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.exception.SubGoalException;
import com.hotpack.krocs.domain.goals.exception.SubGoalExceptionType;
import com.hotpack.krocs.domain.goals.repository.SubGoalRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubGoalRepositoryFacade {

  private final SubGoalRepository subGoalRepository;

  @Transactional
  public List<SubGoal> saveSubGoals(List<SubGoal> subGoals) {
    return subGoalRepository.saveAll(subGoals);
  }

  @Transactional
  public SubGoal saveSubGoal(SubGoal subGoal) {
    return subGoalRepository.save(subGoal);
  }

  public List<SubGoal> findSubGoalsByGoal(Goal goal) {
    List<SubGoal> subGoals = subGoalRepository.findSubGoalsByGoal(goal);
    if (subGoals.isEmpty()) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
    }

    return subGoals;
  }

  public SubGoal findSubGoalBySubGoalId(Long subGoalId) {
    SubGoal subGoal = subGoalRepository.findSubGoalsBySubGoalId(subGoalId);
    if (subGoal == null) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
    }

    return subGoal;
  }

  @Transactional
  public void deleteSubGoalBySubGoalId(Long subGoalId) {
    findSubGoalBySubGoalId(subGoalId);
    subGoalRepository.deleteSubGoalBySubGoalId(subGoalId);
  }
}
