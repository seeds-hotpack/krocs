package com.hotpack.krocs.domain.plan.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.plans.converter.PlanConverter;
import com.hotpack.krocs.domain.plans.converter.SubPlanConverter;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PlanConverterTest {
    private PlanConverter planConverter;
    private Goal validGoal;
    private SubGoal validSubGoal;
    private PlanCreateRequestDTO validRequestDTO;
    private Plan validPlan;
    private SubPlanConverter subPlanConverter;

    @BeforeEach
    void setUp() {
        planConverter = new PlanConverter(subPlanConverter);

        validGoal = Goal.builder()
                .goalId(1L)
                .title("테스트 목표")
                .build();

        validSubGoal = SubGoal.builder()
            .subGoalId(1L)
            .goal(validGoal)
            .title("테스트 서브목표")
            .isCompleted(false)
            .build();

        validRequestDTO = PlanCreateRequestDTO.builder()
                .title("테스트 일정")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .build();

        validPlan = Plan.builder()
                .planId(1L)
                .goal(validGoal)
                .subGoal(validSubGoal)
                .title("테스트 일정")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .isCompleted(false)
                .build();
    }

    @Test
    @DisplayName("RequestDTO를 Plan 엔티티로 변환")
    void toEntity_Success() {
        // when
        Plan result = planConverter.toEntity(validRequestDTO, validGoal, validSubGoal);

        // then
        assertThat(result.getTitle()).isEqualTo("테스트 일정");
        assertThat(result.getGoal()).isEqualTo(validGoal);
        assertThat(result.getSubGoal()).isEqualTo(validSubGoal);
        assertThat(result.getStartDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 9, 0));
        assertThat(result.getEndDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 10, 0));
        assertThat(result.getAllDay()).isFalse();
        assertThat(result.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("Plan 엔티티를 ResponseDTO로 변환")
    void toCreateResponseDTO_Success() {
        // when
        PlanResponseDTO result = planConverter.toEntity(validPlan);

        // then
        assertThat(result.getPlanId()).isEqualTo(1L);
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 일정");
        assertThat(result.getSubGoalId()).isEqualTo(1L);
        assertThat(result.getStartDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 9, 0));
        assertThat(result.getEndDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 10, 0));
        assertThat(result.getAllDay()).isFalse();
        assertThat(result.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("allDay = true인 경우 변환")
    void toEntity_AllDay_True() {
        // given
        PlanCreateRequestDTO allDayRequest = PlanCreateRequestDTO.builder()
                .title("하루 종일 일정")
                .allDay(true)
                .build();

        // when
        Plan result = planConverter.toEntity(allDayRequest, validGoal, validSubGoal);

        // then
        assertThat(result.getAllDay()).isTrue();
        assertThat(result.getStartDateTime()).isNull();
        assertThat(result.getEndDateTime()).isNull();
    }

    @Test
    @DisplayName("Plan 객체가 null인 경우")
    void toCreateResponseDTO_PlanNull() {
        // when & then
        assertThatThrownBy(() -> planConverter.toEntity(null))
            .isInstanceOf(NullPointerException.class);
    }
}
