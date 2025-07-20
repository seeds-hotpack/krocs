package com.hotpack.krocs.repository.goals;

import com.hotpack.krocs.common.entity.goals.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoalRepository extends JpaRepository<Goal, Long> {

}
