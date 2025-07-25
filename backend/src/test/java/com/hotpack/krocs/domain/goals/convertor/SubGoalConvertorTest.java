package com.hotpack.krocs.domain.goals.convertor;

import static org.assertj.core.api.Assertions.assertThat;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalUpdateResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubGoalConvertorTest {

  private SubGoal validSubGoal;

  @BeforeEach
  void setUp() {
    Goal validGoal = Goal.builder()
        .goalId(1L)
        .title("테스트 목표")
        .priority(Priority.HIGH)
        .startDate(LocalDate.of(2024, 1, 1))
        .endDate(LocalDate.of(2024, 12, 31))
        .duration(365)
        .isCompleted(false)
        .build();

    validSubGoal = SubGoal.builder()
        .goal(validGoal)
        .title("테스트 소목표")
        .isCompleted(false)
        .subGoalId(1L)
        .build();
  }

  @Test
  @DisplayName("SubGoal을 SubGoalUpdateResponseDTO로 변환")
  void toSubGoalUpdateResponseDTO_Success() {

    // when
    SubGoalUpdateResponseDTO responseDTO = SubGoalConvertor.toSubGoalUpdateResponseDTO(
        validSubGoal);

    // then
    assertThat(responseDTO.subGoalId()).isEqualTo(validSubGoal.getSubGoalId());
    assertThat(responseDTO.goalId()).isEqualTo(validSubGoal.getGoal().getGoalId());
    assertThat(responseDTO.title()).isEqualTo(validSubGoal.getTitle());
    assertThat(responseDTO.isCompleted()).isEqualTo(validSubGoal.getIsCompleted());
  }
}