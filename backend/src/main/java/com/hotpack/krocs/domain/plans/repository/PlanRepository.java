package com.hotpack.krocs.domain.plans.repository;

import com.hotpack.krocs.domain.plans.domain.Plan;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    @Query("SELECT p FROM Plan p WHERE :date BETWEEN p.startDateTime AND p.endDateTime")
    List<Plan> findPlans(@Param("date") LocalDateTime dateTime);
}
