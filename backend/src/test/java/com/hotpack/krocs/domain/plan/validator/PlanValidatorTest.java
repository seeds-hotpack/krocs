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
            .build();

        // when & then
        assertThatCode(() -> planValidator.validatePlanCreation(validRequest, 1L))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효성 검사 성공 - allDay = true, 같은 날짜")
    void validatePlanCreation_Success_AllDayTrue_SameDate() {
        // given
        PlanCreateRequestDTO allDayRequest = PlanCreateRequestDTO.builder()
            .title("하루 종일 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 3, 10, 30)) // 시간은 무시됨
            .endDateTime(LocalDateTime.of(2025, 8, 3, 15, 45))   // 시간은 무시됨
            .allDay(true)
            .build();

        // when & then
        assertThatCode(() -> planValidator.validatePlanCreation(allDayRequest, 1L))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효성 검사 성공 - allDay = true, 여러 날짜")
    void validatePlanCreation_Success_AllDayTrue_MultipleDays() {
        // given
        PlanCreateRequestDTO allDayRequest = PlanCreateRequestDTO.builder()
            .title("여러 날 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 3, 23, 59)) // 시간은 무시됨
            .endDateTime(LocalDateTime.of(2025, 8, 5, 0, 1))     // 시간은 무시됨
            .allDay(true)
            .build();

        // when & then
        assertThatCode(() -> planValidator.validatePlanCreation(allDayRequest, 1L))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("유효성 검사 성공 - subGoalId가 null")
    void validatePlanCreation_Success_NullSubGoalId() {
        // given
        PlanCreateRequestDTO validRequest = PlanCreateRequestDTO.builder()
            .title("서브골 없는 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
            .allDay(false)
            .build();

        // when & then
        assertThatCode(() -> planValidator.validatePlanCreation(validRequest, null))
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
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_START_TIME_REQUIRED);
    }

    @Test
    @DisplayName("유효성 검사 실패 - allDay = false인데 종료시간 누락")
    void validatePlanCreation_Fail_NoEndTime() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title("시간 지정 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .allDay(false)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_END_TIME_REQUIRED);
    }

    @Test
    @DisplayName("유효성 검사 실패 - allDay = true인데 시작시간 누락")
    void validatePlanCreation_Fail_AllDayTrue_NoStartTime() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title("하루 종일 일정")
            .endDateTime(LocalDateTime.of(2025, 8, 1, 0, 0))
            .allDay(true)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_START_TIME_REQUIRED);
    }

    @Test
    @DisplayName("유효성 검사 실패 - allDay = true인데 종료시간 누락")
    void validatePlanCreation_Fail_AllDayTrue_NoEndTime() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title("하루 종일 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 0, 0))
            .allDay(true)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_END_TIME_REQUIRED);
    }

    @Test
    @DisplayName("유효성 검사 실패 - allDay = false, 시작시간이 종료시간보다 늦음")
    void validatePlanCreation_Fail_AllDayFalse_InvalidTimeRange() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title("시간 순서 오류")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .allDay(false)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.INVALID_PLAN_DATE_RANGE);
    }

    @Test
    @DisplayName("유효성 검사 실패 - allDay = true, 시작날짜가 종료날짜보다 늦음")
    void validatePlanCreation_Fail_AllDayTrue_InvalidDateRange() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title("날짜 순서 오류")
            .startDateTime(LocalDateTime.of(2025, 8, 5, 1, 0))  // 시간은 무시되고 날짜만 비교
            .endDateTime(LocalDateTime.of(2025, 8, 3, 23, 0))   // 시간은 무시되고 날짜만 비교
            .allDay(true)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.INVALID_PLAN_DATE_RANGE);
    }

    @Test
    @DisplayName("유효성 검사 실패 - 제목이 빈 문자열")
    void validatePlanCreation_Fail_EmptyTitle() {
        // given
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title("")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
            .allDay(false)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_TITLE_EMPTY);
    }

    @Test
    @DisplayName("유효성 검사 실패 - 제목이 200자 초과")
    void validatePlanCreation_Fail_TitleTooLong() {
        // given
        String longTitle = "a".repeat(201); // 201자
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title(longTitle)
            .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
            .allDay(false)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(invalidRequest, 1L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_TITLE_TOO_LONG);
    }

    @Test
    @DisplayName("유효성 검사 실패 - subGoalId가 0 이하")
    void validatePlanCreation_Fail_InvalidSubGoalId() {
        // given
        PlanCreateRequestDTO validRequest = PlanCreateRequestDTO.builder()
            .title("테스트 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
            .allDay(false)
            .build();

        // when & then
        assertThatThrownBy(() -> planValidator.validatePlanCreation(validRequest, 0L))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.GOAL_INVALID_GOAL_ID);
    }

    @Test
    @DisplayName("유효성 검사 성공 - allDay가 null인 경우 (기본값 false로 처리)")
    void validatePlanCreation_Success_AllDayNull() {
        // given
        PlanCreateRequestDTO validRequest = PlanCreateRequestDTO.builder()
            .title("테스트 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
            .allDay(null) // null인 경우
            .build();

        // when & then
        assertThatCode(() -> planValidator.validatePlanCreation(validRequest, 1L))
            .doesNotThrowAnyException();
    }
}