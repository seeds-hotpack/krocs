package com.hotpack.krocs.domain.goals.convertor;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class GoalConvertorTest {

    private GoalConvertor goalConvertor;

    private CreateGoalRequestDTO validRequestDTO;
    private Goal validGoal;

    @BeforeEach
    void setUp() {
        goalConvertor = new GoalConvertor();

        validRequestDTO = CreateGoalRequestDTO.builder()
                .title("테스트 목표")
                .priority(Priority.HIGH)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .duration(365)
                .build();

        validGoal = Goal.builder()
                .goalId(1L)
                .title("테스트 목표")
                .priority(Priority.HIGH)
                .startDate(LocalDate.of(2024, 1, 1))
                .endDate(LocalDate.of(2024, 12, 31))
                .duration(365)
                .isCompleted(false)
                .build();
    }

    @Test
    @DisplayName("CreateGoalRequestDTO를 Goal 엔티티로 변환")
    void toEntity_Success() {
        // when
        Goal result = goalConvertor.toEntity(validRequestDTO);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("테스트 목표");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(result.getDuration()).isEqualTo(365);
        assertThat(result.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("Goal 엔티티를 CreateGoalResponseDTO로 변환")
    void toCreateResponseDTO_Success() {
        // when
        CreateGoalResponseDTO result = goalConvertor.toCreateResponseDTO(validGoal);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 목표");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2024, 1, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2024, 12, 31));
        assertThat(result.getDuration()).isEqualTo(365);
        assertThat(result.isCompleted()).isFalse();
        assertThat(result.getCompletionPercentage()).isEqualTo(0);
    }

    @Test
    @DisplayName("최소 데이터로 DTO를 엔티티로 변환")
    void toEntity_MinimalData() {
        // given
        CreateGoalRequestDTO minimalRequest = CreateGoalRequestDTO.builder()
                .title("최소 목표")
                .duration(1)
                .build();

        // when
        Goal result = goalConvertor.toEntity(minimalRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("최소 목표");
        assertThat(result.getPriority()).isEqualTo(Priority.MEDIUM); // 기본값
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
        assertThat(result.getDuration()).isEqualTo(1);
        assertThat(result.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("null 값이 포함된 DTO를 엔티티로 변환")
    void toEntity_WithNullValues() {
        // given
        CreateGoalRequestDTO requestWithNulls = CreateGoalRequestDTO.builder()
                .title("null 테스트")
                .priority(null)
                .startDate(null)
                .endDate(null)
                .duration(30)
                .build();

        // when
        Goal result = goalConvertor.toEntity(requestWithNulls);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("null 테스트");
        assertThat(result.getPriority()).isEqualTo(Priority.MEDIUM); // 기본값
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
        assertThat(result.getDuration()).isEqualTo(30);
    }

    @Test
    @DisplayName("완료된 목표를 응답 DTO로 변환")
    void toCreateResponseDTO_CompletedGoal() {
        // given
        Goal completedGoal = Goal.builder()
                .goalId(1L)
                .title("완료된 목표")
                .priority(Priority.HIGH)
                .duration(30)
                .isCompleted(true)
                .build();

        // when
        CreateGoalResponseDTO result = goalConvertor.toCreateResponseDTO(completedGoal);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("완료된 목표");
        assertThat(result.isCompleted()).isTrue();
        assertThat(result.getCompletionPercentage()).isEqualTo(100);
    }

    @Test
    @DisplayName("null 값이 포함된 Goal을 응답 DTO로 변환")
    void toCreateResponseDTO_WithNullValues() {
        // given
        Goal goalWithNulls = Goal.builder()
                .goalId(1L)
                .title("null 테스트")
                .priority(null)
                .startDate(null)
                .endDate(null)
                .duration(30)
                .isCompleted(false)
                .build();

        // when
        CreateGoalResponseDTO result = goalConvertor.toCreateResponseDTO(goalWithNulls);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("null 테스트");
        assertThat(result.getPriority()).isNull();
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
        assertThat(result.getDuration()).isEqualTo(30);
        assertThat(result.isCompleted()).isFalse();
    }
} 