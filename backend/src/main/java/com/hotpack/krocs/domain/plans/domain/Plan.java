package com.hotpack.krocs.domain.plans.domain;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.dto.request.GoalUpdateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.request.PlanUpdateRequestDTO;
import com.hotpack.krocs.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "plans")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Plan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Goal goal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_goal_id")
    private SubGoal subGoal;

    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubPlan> subPlans;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "start_datetime")
    private LocalDateTime startDateTime;

    @Column(name = "end_datetime")
    private LocalDateTime endDateTime;

    @Column(name = "all_day", nullable = false)
    @Builder.Default
    private Boolean allDay = false;

    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;

    // 미루기 기능
    // @Column(name = "is_snoozed", nullable = false)
    // @Builder.Default
    // private Boolean isSnoozed = false;

    // @Column(name = "snooze_until")
    // private LocalDateTime snoozeUntil;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void updateFrom(PlanUpdateRequestDTO request) {
        if (request.getTitle() != null) {
            this.title = request.getTitle();
        }

        if (request.getStartDateTime() != null) {
            this.startDateTime = request.getStartDateTime();
        }

        if (request.getEndDateTime() != null) {
            this.endDateTime = request.getEndDateTime();
        }

        if (request.getAllDay() != null) {
            this.allDay = request.getAllDay();
        }

        if (request.getIsCompleted() != null) {
            this.isCompleted = request.getIsCompleted();
            if (request.getIsCompleted()) {
                this.completedAt = LocalDateTime.now();
            } else {
                this.completedAt = null;
            }
        }
    }
}