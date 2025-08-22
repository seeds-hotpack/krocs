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
    @Query("SELECT p FROM Plan p WHERE p.user.userId = :userId AND p.startDateTime <= :endOfDay AND p.endDateTime >= :startOfDay")
    List<Plan> findPlansByDateRange(
        @Param("startOfDay") LocalDateTime startOfDay,
        @Param("endOfDay") LocalDateTime endOfDay,
        @Param("userId") Long userId // userId 조건도 추가
    );
}
