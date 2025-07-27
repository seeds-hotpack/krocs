package com.hotpack.krocs.domain.goals.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.GoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.GoalUpdateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalCreateResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GoalConvertorTest {

    private GoalConverter goalConvertor;

    private GoalCreateRequestDTO validRequestDTO;
    private Goal validGoal;

    @BeforeEach
    void setUp() {
        goalConvertor = new GoalConverter();

        validRequestDTO = GoalCreateRequestDTO.builder()
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

    // ========== CreateGoalRequestDTO 변환 테스트 ==========

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
    @DisplayName("Goal 엔티티를 GoalCreateResponseDTO로 변환")
    void toCreateResponseDTO_Success() {
        // when
        GoalCreateResponseDTO result = goalConvertor.toCreateResponseDTO(validGoal);

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
        GoalCreateRequestDTO minimalRequest = GoalCreateRequestDTO.builder()
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
        GoalCreateRequestDTO requestWithNulls = GoalCreateRequestDTO.builder()
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
        GoalCreateResponseDTO result = goalConvertor.toCreateResponseDTO(completedGoal);

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
        GoalCreateResponseDTO result = goalConvertor.toCreateResponseDTO(goalWithNulls);

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

    // ========== UpdateGoalRequestDTO 변환 테스트 ==========

    @Test
    @DisplayName("UpdateGoalRequestDTO를 Goal 엔티티로 변환")
    void toEntity_UpdateRequest_Success() {
        // given
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .title("수정된 목표")
                .priority(Priority.LOW)
                .startDate(LocalDate.of(2025, 6, 1))
                .endDate(LocalDate.of(2025, 6, 30))
                .duration(30)
                .build();

        // when
        Goal result = goalConvertor.toEntity(updateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("수정된 목표");
        assertThat(result.getPriority()).isEqualTo(Priority.LOW);
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2025, 6, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2025, 6, 30));
        assertThat(result.getDuration()).isEqualTo(30);
        assertThat(result.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("UpdateGoalRequestDTO 부분 변환 - null 필드들")
    void toEntity_UpdateRequest_PartialData() {
        // given
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .title("부분 수정")
                .priority(null)
                .startDate(null)
                .endDate(null)
                .duration(null)
                .build();

        // when
        Goal result = goalConvertor.toEntity(updateRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("부분 수정");
        assertThat(result.getPriority()).isNull();
        assertThat(result.getStartDate()).isNull();
        assertThat(result.getEndDate()).isNull();
        assertThat(result.getDuration()).isNull();
        assertThat(result.getIsCompleted()).isFalse();
    }

    // ========== toGoalResponseDTO 테스트 ==========

    @Test
    @DisplayName("Goal 엔티티를 GoalResponseDTO로 변환")
    void toGoalResponseDTO_Success() {
        // given
        Goal goal = Goal.builder()
                .goalId(2L)
                .title("GoalResponseDTO 테스트")
                .priority(Priority.CRITICAL)
                .startDate(LocalDate.of(2025, 8, 1))
                .endDate(LocalDate.of(2025, 8, 31))
                .duration(31)
                .isCompleted(true)
                .build();

        // when
        GoalResponseDTO result = goalConvertor.toGoalResponseDTO(goal);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalId()).isEqualTo(2L);
        assertThat(result.getTitle()).isEqualTo("GoalResponseDTO 테스트");
        assertThat(result.getPriority()).isEqualTo(Priority.CRITICAL);
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2025, 8, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2025, 8, 31));
        assertThat(result.getDuration()).isEqualTo(31);
        assertThat(result.getIsCompleted()).isTrue();
        assertThat(result.getCompletionPercentage()).isEqualTo(100);
    }

    @Test
    @DisplayName("List<Goal>을 List<GoalResponseDTO>로 변환")
    void toGoalResponseDTO_List_Success() {
        // given
        Goal goal1 = Goal.builder()
                .goalId(1L)
                .title("목표 1")
                .priority(Priority.HIGH)
                .duration(30)
                .isCompleted(false)
                .build();

        Goal goal2 = Goal.builder()
                .goalId(2L)
                .title("목표 2")
                .priority(Priority.LOW)
                .duration(60)
                .isCompleted(true)
                .build();

        List<Goal> goals = Arrays.asList(goal1, goal2);

        // when
        List<GoalResponseDTO> result = goalConvertor.toGoalResponseDTO(goals);

        // then
        assertThat(result).hasSize(2);

        // 첫 번째 목표 검증
        GoalResponseDTO firstResult = result.get(0);
        assertThat(firstResult.getGoalId()).isEqualTo(1L);
        assertThat(firstResult.getTitle()).isEqualTo("목표 1");
        assertThat(firstResult.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(firstResult.getIsCompleted()).isFalse();
        assertThat(firstResult.getCompletionPercentage()).isEqualTo(0);

        // 두 번째 목표 검증
        GoalResponseDTO secondResult = result.get(1);
        assertThat(secondResult.getGoalId()).isEqualTo(2L);
        assertThat(secondResult.getTitle()).isEqualTo("목표 2");
        assertThat(secondResult.getPriority()).isEqualTo(Priority.LOW);
        assertThat(secondResult.getIsCompleted()).isTrue();
        assertThat(secondResult.getCompletionPercentage()).isEqualTo(100);
    }

    @Test
    @DisplayName("빈 Goal 리스트를 빈 GoalResponseDTO 리스트로 변환")
    void toGoalResponseDTO_EmptyList() {
        // given
        List<Goal> emptyGoals = Collections.emptyList();

        // when
        List<GoalResponseDTO> result = goalConvertor.toGoalResponseDTO(emptyGoals);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    // ========== 완료율 계산 테스트 ==========

    @Test
    @DisplayName("완료율 계산 - SubGoal 없는 경우")
    void calculateCompletionPercentage_NoSubGoals() {
        // given - 미완료 목표
        Goal incompleteGoal = Goal.builder()
                .goalId(1L)
                .title("서브 목표 없는 미완료 목표")
                .isCompleted(false)
                .subGoals(null)
                .build();

        // when
        GoalCreateResponseDTO result1 = goalConvertor.toCreateResponseDTO(incompleteGoal);

        // then
        assertThat(result1.getCompletionPercentage()).isEqualTo(0);

        // given - 완료 목표
        Goal completeGoal = Goal.builder()
                .goalId(2L)
                .title("서브 목표 없는 완료 목표")
                .isCompleted(true)
                .subGoals(Collections.emptyList())
                .build();

        // when
        GoalCreateResponseDTO result2 = goalConvertor.toCreateResponseDTO(completeGoal);

        // then
        assertThat(result2.getCompletionPercentage()).isEqualTo(100);
    }

    // ========== 날짜/시간 필드 테스트 ==========

    @Test
    @DisplayName("null createdAt, updatedAt 필드 처리")
    void dateTimeFields_Null() {
        // given
        Goal goalWithNullDates = Goal.builder()
                .goalId(1L)
                .title("null 날짜 테스트")
                .priority(Priority.LOW)
                .duration(30)
                .isCompleted(false)
                .build();
        // createdAt, updatedAt는 null로 남겨둠

        // when
        GoalCreateResponseDTO result = goalConvertor.toCreateResponseDTO(goalWithNullDates);

        // then
        assertThat(result.getCreatedAt()).isNull();
        assertThat(result.getUpdatedAt()).isNull();
    }
} 