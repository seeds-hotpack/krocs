package com.hotpack.krocs.domain.goals.domain;

import com.hotpack.krocs.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_goals")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubGoal extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_goal_id")
    private Long subGoalId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id", nullable = false)
    private Goal goal;

    @Column(name = "title", nullable = false, length = 200)
    private String title;


    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;
}