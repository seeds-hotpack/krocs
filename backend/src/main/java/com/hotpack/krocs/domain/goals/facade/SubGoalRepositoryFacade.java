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

  /**
   * 주어진 Goal에 속한 모든 소목표를 조회합니다.
   *
   * <p>소목표가 하나도 없을 경우 SUB_GOAL_NOT_FOUND 예외를 던집니다.
   *
   * @param goal 소속된 Goal
   * @return 해당 Goal에 연결된 소목표 리스트
   * @throws SubGoalException SUB_GOAL_NOT_FOUND (소목표가 없는 경우)
   */
  public List<SubGoal> findSubGoalsByGoal(Goal goal) {
    List<SubGoal> subGoals = subGoalRepository.findSubGoalsByGoal(goal);
    if (subGoals.isEmpty()) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
    }

    return subGoals;
  }


  /**
   * 주어진 ID에 해당하는 소목표를 조회합니다.
   *
   * <p>null인 경우 예외를 던집니다.
   *
   * @param subGoalId 조회할 소목표의 ID
   * @return 해당 ID에 해당하는 SubGoal
   * @throws SubGoalException SUB_GOAL_NOT_FOUND (해당 ID가 존재하지 않는 경우)
   */
  public SubGoal findSubGoalBySubGoalId(Long subGoalId) {
    SubGoal subGoal = subGoalRepository.findSubGoalsBySubGoalId(subGoalId);
    if (subGoal == null) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
    }

    return subGoal;
  }

  /**
   * 주어진 ID에 해당하는 소목표를 삭제합니다.
   *
   * <p>삭제 전 해당 소목표 존재 여부를 먼저 검증합니다.
   *
   * @param subGoalId 삭제할 소목표의 ID
   * @throws SubGoalException SUB_GOAL_NOT_FOUND (삭제 대상이 존재하지 않는 경우)
   */
  @Transactional
  public void deleteSubGoalBySubGoalId(Long subGoalId) {
    findSubGoalBySubGoalId(subGoalId);
    subGoalRepository.deleteSubGoalBySubGoalId(subGoalId);
  }
}
