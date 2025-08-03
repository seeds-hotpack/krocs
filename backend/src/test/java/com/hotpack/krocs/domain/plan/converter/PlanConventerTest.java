package com.hotpack.krocs.domain.plan.converter;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.plans.converter.PlanConverter;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PlanConventerTest {
    private PlanConverter planConverter;
    private Goal validGoal;
    private SubGoal validSubGoal;
    private PlanCreateRequestDTO validRequestDTO;
    private Plan validPlan;

    @BeforeEach
    void setUp() {
        planConverter = new PlanConverter();

        validGoal = Goal.builder()
                .goalId(1L)
                .title("테스트 목표")
                .build();

        validSubGoal = SubGoal.builder()
            .subGoalId(1L)
            .title("테스트 서브목표")
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
        assertThat(result.getStartDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 9, 0));
        assertThat(result.getEndDateTime()).isEqualTo(LocalDateTime.of(2025, 8, 1, 10, 0));
        assertThat(result.getAllDay()).isFalse();
        assertThat(result.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("Plan 엔티티를 ResponseDTO로 변환")
    void toCreateResponseDTO_Success() {
        // when
        PlanCreateResponseDTO result = planConverter.toCreateResponseDTO(validPlan);

        // then
        assertThat(result.getPlanId()).isEqualTo(1L);
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 일정");
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
}
