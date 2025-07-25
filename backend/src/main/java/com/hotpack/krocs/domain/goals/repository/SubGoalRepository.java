package com.hotpack.krocs.domain.goals.repository;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubGoalRepository extends JpaRepository<SubGoal, Long> {

  List<SubGoal> findSubGoalsByGoal(Goal goal);

  SubGoal findSubGoalsBySubGoalId(Long subGoalId);
}
