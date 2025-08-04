package com.hotpack.krocs.domain.plans.service;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.facade.SubGoalRepositoryFacade;
import com.hotpack.krocs.domain.plans.converter.PlanConverter;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanResponseDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import com.hotpack.krocs.domain.plans.facade.PlanRepositoryFacade;
import com.hotpack.krocs.domain.plans.validator.PlanValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {
    @Mock
    private PlanRepositoryFacade planRepositoryFacade;
    @Mock
    private PlanConverter planConverter;
    @Mock
    private SubGoalRepositoryFacade subGoalRepositoryFacade;
    @Mock
    private PlanValidator planValidator;

    @InjectMocks
    private PlanServiceImpl planService;

    // 테스트 데이터
    private PlanCreateRequestDTO validRequestDTO;
    private Plan validPlan;
    private Goal validGoal;
    private SubGoal validSubGoal;
    private PlanResponseDTO validResponseDTO;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
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

        validResponseDTO = PlanResponseDTO.builder()
                .planId(1L)
                .goalId(1L)
                .subGoalId(1L)
                .title("테스트 일정")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .isCompleted(false)
                .build();
    }

    // ========== CREATE 테스트 ==========

    @Test
    @DisplayName("일정 생성 성공")
    void createPlan_Success() {
        // given
        Long userId = 1L;
        Long subGoalId = 1L;

        doNothing().when(planValidator).validatePlanCreation(validRequestDTO, subGoalId);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        when(planConverter.toEntity(validRequestDTO, validGoal, validSubGoal)).thenReturn(validPlan);
        when(planRepositoryFacade.savePlan(validPlan)).thenReturn(validPlan);
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.createPlan(validRequestDTO, userId, subGoalId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPlanId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 일정");
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getSubGoalId()).isEqualTo(1L); // SubGoalId 검증도 추가
    }

    @Test
    @DisplayName("일정 생성 실패 - SubGoal이 존재하지 않음")
    void createPlan_Fail_SubGoalNotFound() {
        // given
        Long userId = 1L;
        Long subGoalId = 999L; // goalId → subGoalId 변경

        doNothing().when(planValidator).validatePlanCreation(validRequestDTO, subGoalId);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(null); // Mock 변경

        // when & then
        assertThatThrownBy(() -> planService.createPlan(validRequestDTO, userId, subGoalId)) // 파라미터 변경
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_SUB_GOAL_NOT_FOUND); // 예외 타입 변경
    }

    @Test
    @DisplayName("일정 생성 실패 - 유효성 검사 실패")
    void createPlan_Fail_ValidationError() {
        // given
        Long userId = 1L;
        Long subGoalId = 1L;

        doThrow(new PlanException(PlanExceptionType.PLAN_TITLE_EMPTY))
                .when(planValidator).validatePlanCreation(validRequestDTO, subGoalId);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(validRequestDTO, userId, subGoalId))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_TITLE_EMPTY);
    }

    @Test
    @DisplayName("일정 생성 성공 - allDay = true")
    void createPlan_Success_AllDay() {
        // given
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanCreateRequestDTO allDayRequest = PlanCreateRequestDTO.builder()
            .title("하루 종일 일정")
            .allDay(true)
            .build();

        Plan allDayPlan = Plan.builder()
            .planId(1L)
            .goal(validGoal)
            .subGoal(validSubGoal)
            .title("하루 종일 일정")
            .allDay(true)
            .isCompleted(false)
            .build();

        PlanResponseDTO allDayResponse = PlanResponseDTO.builder()
            .planId(1L)
            .goalId(1L)
            .subGoalId(1L)
            .title("하루 종일 일정")
            .allDay(true)
            .isCompleted(false)
            .build();

        doNothing().when(planValidator).validatePlanCreation(allDayRequest, subGoalId);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        when(planConverter.toEntity(allDayRequest, validGoal, validSubGoal)).thenReturn(allDayPlan);
        when(planRepositoryFacade.savePlan(allDayPlan)).thenReturn(allDayPlan);
        when(planConverter.toEntity(allDayPlan)).thenReturn(allDayResponse);

        // when
        PlanResponseDTO result = planService.createPlan(allDayRequest, userId, subGoalId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("하루 종일 일정");
        assertThat(result.getAllDay()).isTrue();
        assertThat(result.getStartDateTime()).isNull();  // allDay = true면 시간은 null
        assertThat(result.getEndDateTime()).isNull();
    }

    @Test
    @DisplayName("일정 생성 실패 - allDay = false인데 시간 누락")
    void createPlan_Fail_AllDayFalse_NoDateTime() {
        // given
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
            .title("시간 지정 일정")
            .allDay(false)
            // startDateTime, endDateTime 누락
            .build();

        doThrow(new PlanException(PlanExceptionType.PLAN_START_TIME_REQUIRED))
            .when(planValidator).validatePlanCreation(invalidRequest, subGoalId);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(invalidRequest, userId, subGoalId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_START_TIME_REQUIRED);

        verify(planValidator).validatePlanCreation(invalidRequest, subGoalId);
        verify(subGoalRepositoryFacade, never()).findSubGoalBySubGoalId(any());
        verify(subGoalRepositoryFacade, never()).findGoalBySubGoalId(any());
        verify(planConverter, never()).toEntity(any(), any(), any());
    }

    @Test
    @DisplayName("일정 생성 실패 - 시간 순서 오류")
    void createPlan_Fail_InvalidTimeOrder() {
        // given
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanCreateRequestDTO invalidTimeRequest = PlanCreateRequestDTO.builder()
            .title("시간 순서 오류")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))  // 종료시간보다 늦음
            .endDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
            .allDay(false)
            .build();

        doThrow(new PlanException(PlanExceptionType.INVALID_PLAN_DATE_RANGE))
            .when(planValidator).validatePlanCreation(invalidTimeRequest, subGoalId);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(invalidTimeRequest, userId, subGoalId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.INVALID_PLAN_DATE_RANGE);
    }

    @Test
    @DisplayName("일정 생성 실패 - Repository에서 예외 발생")
    void createPlan_Fail_RepositoryException() {
        // given
        Long userId = 1L;
        Long subGoalId = 1L;

        doNothing().when(planValidator).validatePlanCreation(validRequestDTO, subGoalId);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        when(planConverter.toEntity(validRequestDTO, validGoal, validSubGoal)).thenReturn(validPlan);
        when(planRepositoryFacade.savePlan(validPlan)).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> planService.createPlan(validRequestDTO, userId, subGoalId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_CREATION_FAILED);
    }
}