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

/**
 * SubGoal Repository Facade 데이터 접근 계층을 추상화합니다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SubGoalRepositoryFacade {

  private final SubGoalRepository subGoalRepository;

  /**
   * 소목표(SubGoal)를 리스트로 입력 받아 모두 DB에 저장합니다.
   *
   * @param subGoals
   * @return
   */
  @Transactional
  public List<SubGoal> saveSubGoals(List<SubGoal> subGoals) {
    return subGoalRepository.saveAll(subGoals);
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
    System.out.println("subGoal = " + subGoal); // null이면 바로 알 수 있어
    if (subGoal == null) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
    }

    return subGoal;
  }
}
