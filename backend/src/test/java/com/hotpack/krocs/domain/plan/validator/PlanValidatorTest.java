package com.hotpack.krocs.domain.plan.validator;

import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import com.hotpack.krocs.domain.plans.validator.PlanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class PlanValidatorTest {
    private PlanValidator planValidator;

    @BeforeEach
    void setUp() {
        planValidator = new PlanValidator();
    }

    @Test
    @DisplayName("유효성 검사 성공 - allDay = false")
    void validatePlanCreation_Success_AllDayFalse() {
        // given
        PlanCreateRequestDTO validRequest = PlanCreateRequestDTO.builder()
                .title("테스트 일정")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .energy(5)
                .build();

        // when & then
        assertThatCode(() -> planValidator.validatePlanCreation(validRequest, 1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효성 검사 성공 - allDay = true")
    void validatePlanCreation_Success_AllDayTrue() {
        // given
        PlanCreateRequestDTO allDayRequest = PlanCreateRequestDTO.builder()
                .title("하루 종일 일정")
                .allDay(true)
                .energy(3)
                .build();

        // when & then
        assertThatCode(() -> planValidator.validatePlanCreation(allDayRequest, 1L))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효성 검사 실패 - allDay = false인데 시작시간 누락")
    void validatePlanCreation_Fail_NoStartTime() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
                .title("시간 지정 일정")
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .energy(5)
                .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_START_TIME_REQUIRED);
    }

    @Test
    @DisplayName("유효성 검사 실패 - 시작시간이 종료시간보다 늦음")
    void validatePlanCreation_Fail_InvalidTimeRange() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
                .title("시간 순서 오류")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .allDay(false)
                .energy(5)
                .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.INVALID_PLAN_DATE_RANGE);
    }
}
