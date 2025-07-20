package com.hotpack.krocs.repository.goals;

import com.hotpack.krocs.common.entity.goals.SubGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubGoalRepository extends JpaRepository<SubGoal, Long> {

}
