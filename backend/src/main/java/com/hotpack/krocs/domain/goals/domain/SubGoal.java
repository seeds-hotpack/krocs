package com.hotpack.krocs.domain.goals.domain;

import com.hotpack.krocs.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @Setter
  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Setter
  @Column(name = "is_completed", nullable = false)
  @Builder.Default
  private Boolean isCompleted = false;
}