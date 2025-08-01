package com.hotpack.krocs.domain.plan.service;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.GoalRepositoryFacade;
import com.hotpack.krocs.domain.plans.converter.PlanConverter;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import com.hotpack.krocs.domain.plans.facade.PlanRepositoryFacade;
import com.hotpack.krocs.domain.plans.service.PlanServiceImpl;
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
    private GoalRepositoryFacade goalRepositoryFacade;
    @Mock
    private PlanValidator planValidator;

    @InjectMocks
    private PlanServiceImpl planService;

    // 테스트 데이터
    private PlanCreateRequestDTO validRequestDTO;
    private Plan validPlan;
    private Goal validGoal;
    private PlanCreateResponseDTO validResponseDTO;

    @BeforeEach
    void setUp() {
        // 테스트 데이터 초기화
        validGoal = Goal.builder()
                .goalId(1L)
                .title("테스트 목표")
                .build();

        validRequestDTO = PlanCreateRequestDTO.builder()
                .title("테스트 일정")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .energy(5)
                .build();

        validPlan = Plan.builder()
                .planId(1L)
                .goal(validGoal)
                .title("테스트 일정")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .energy(5)
                .isCompleted(false)
                .build();

        validResponseDTO = PlanCreateResponseDTO.builder()
                .planId(1L)
                .goalId(1L)
                .title("테스트 일정")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .energy(5)
                .isCompleted(false)
                .build();
    }

    // ========== CREATE 테스트 ==========

    @Test
    @DisplayName("일정 생성 성공")
    void createPlan_Success() {
        // given
        Long userId = 1L;
        Long goalId = 1L;

        doNothing().when(planValidator).validatePlanCreation(validRequestDTO, goalId);
        when(goalRepositoryFacade.findGoalByGoalId(goalId)).thenReturn(validGoal);
        when(planConverter.toEntity(validRequestDTO, validGoal)).thenReturn(validPlan);
        when(planRepositoryFacade.savePlan(validPlan)).thenReturn(validPlan);
        when(planConverter.toCreateResponseDTO(validPlan, goalId)).thenReturn(validResponseDTO);

        // when
        PlanCreateResponseDTO result = planService.createPlan(validRequestDTO, userId, goalId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPlanId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 일정");
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getEnergy()).isEqualTo(5);
    }

    @Test
    @DisplayName("일정 생성 실패 - Goal이 존재하지 않음")
    void createPlan_Fail_GoalNotFound() {
        // given
        Long userId = 1L;
        Long goalId = 999L;

        doNothing().when(planValidator).validatePlanCreation(validRequestDTO, goalId);
        when(goalRepositoryFacade.findGoalByGoalId(goalId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(validRequestDTO, userId, goalId))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_NOT_FOUND);
    }

    @Test
    @DisplayName("일정 생성 실패 - 유효성 검사 실패")
    void createPlan_Fail_ValidationError() {
        // given
        Long userId = 1L;
        Long goalId = 1L;

        doThrow(new PlanException(PlanExceptionType.PLAN_TITLE_EMPTY))
                .when(planValidator).validatePlanCreation(validRequestDTO, goalId);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(validRequestDTO, userId, goalId))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_TITLE_EMPTY);
    }

    // allDay 관련 테스트들
    @Test
    @DisplayName("일정 생성 성공 - allDay = true")
    void createPlan_Success_AllDay() {
        // given
        Long userId = 1L;
        Long goalId = 1L;
        
        PlanCreateRequestDTO allDayRequest = PlanCreateRequestDTO.builder()
                .title("하루 종일 일정")
                .allDay(true)
                .energy(3)
                .build();

        Plan allDayPlan = Plan.builder()
                .planId(1L)
                .goal(validGoal)
                .title("하루 종일 일정")
                .allDay(true)
                .energy(3)
                .isCompleted(false)
                .build();

        PlanCreateResponseDTO allDayResponse = PlanCreateResponseDTO.builder()
                .planId(1L)
                .goalId(1L)
                .title("하루 종일 일정")
                .allDay(true)
                .energy(3)
                .isCompleted(false)
                .build();

        // Mock 설정
        doNothing().when(planValidator).validatePlanCreation(allDayRequest, goalId);
        when(goalRepositoryFacade.findGoalByGoalId(goalId)).thenReturn(validGoal);
        when(planConverter.toEntity(allDayRequest, validGoal)).thenReturn(allDayPlan);
        when(planRepositoryFacade.savePlan(allDayPlan)).thenReturn(allDayPlan);
        when(planConverter.toCreateResponseDTO(allDayPlan, goalId)).thenReturn(allDayResponse);

        // when
        PlanCreateResponseDTO result = planService.createPlan(allDayRequest, userId, goalId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("하루 종일 일정");
        assertThat(result.getAllDay()).isTrue();
        assertThat(result.getEnergy()).isEqualTo(3);
        assertThat(result.getStartDateTime()).isNull();  // allDay = true면 시간은 null
        assertThat(result.getEndDateTime()).isNull();
    }

    @Test
    @DisplayName("일정 생성 실패 - allDay = false인데 시간 누락")
    void createPlan_Fail_AllDayFalse_NoDateTime() {
        // given
        Long userId = 1L;
        Long goalId = 1L;
        
        PlanCreateRequestDTO invalidRequest = PlanCreateRequestDTO.builder()
                .title("시간 지정 일정")
                .allDay(false)
                .energy(5)
                // startDateTime, endDateTime 누락
                .build();

        // PlanValidator에서 예외를 던지도록 설정 (allDay=false인데 시간이 없으므로)
        doThrow(new PlanException(PlanExceptionType.PLAN_START_TIME_REQUIRED))
                .when(planValidator).validatePlanCreation(invalidRequest, goalId);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(invalidRequest, userId, goalId))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_START_TIME_REQUIRED);

        // Validator만 호출되고 나머지는 호출되지 않았는지 확인
        verify(planValidator).validatePlanCreation(invalidRequest, goalId);
        verify(goalRepositoryFacade, never()).findGoalByGoalId(any());
        verify(planConverter, never()).toEntity(any(), any());
    }

    @Test
    @DisplayName("일정 생성 실패 - 시간 순서 오류")
    void createPlan_Fail_InvalidTimeOrder() {
        // given
        Long userId = 1L;
        Long goalId = 1L;
        
        PlanCreateRequestDTO invalidTimeRequest = PlanCreateRequestDTO.builder()
                .title("시간 순서 오류")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))  // 종료시간보다 늦음
                .endDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .allDay(false)
                .energy(5)
                .build();

        // PlanValidator에서 시간 순서 오류 예외를 던지도록 설정
        doThrow(new PlanException(PlanExceptionType.INVALID_PLAN_DATE_RANGE))
                .when(planValidator).validatePlanCreation(invalidTimeRequest, goalId);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(invalidTimeRequest, userId, goalId))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.INVALID_PLAN_DATE_RANGE);
    }

    @Test
    @DisplayName("일정 생성 실패 - 에너지 범위 초과")
    void createPlan_Fail_InvalidEnergy() {
        // given
        Long userId = 1L;
        Long goalId = 1L;
        
        PlanCreateRequestDTO invalidEnergyRequest = PlanCreateRequestDTO.builder()
                .title("에너지 범위 초과")
                .startDateTime(LocalDateTime.of(2025, 8, 1, 9, 0))
                .endDateTime(LocalDateTime.of(2025, 8, 1, 10, 0))
                .allDay(false)
                .energy(15)  // 1-10 범위 초과
                .build();

        // PlanValidator에서 에너지 범위 오류 예외를 던지도록 설정
        doThrow(new PlanException(PlanExceptionType.PLAN_INVALID_ENERGY))
                .when(planValidator).validatePlanCreation(invalidEnergyRequest, goalId);

        // when & then
        assertThatThrownBy(() -> planService.createPlan(invalidEnergyRequest, userId, goalId))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_INVALID_ENERGY);
    }

    @Test
    @DisplayName("일정 생성 실패 - Repository에서 예외 발생")
    void createPlan_Fail_RepositoryException() {
        // given
        Long userId = 1L;
        Long goalId = 1L;

        doNothing().when(planValidator).validatePlanCreation(validRequestDTO, goalId);
        when(goalRepositoryFacade.findGoalByGoalId(goalId)).thenReturn(validGoal);
        when(planConverter.toEntity(validRequestDTO, validGoal)).thenReturn(validPlan);
        when(planRepositoryFacade.savePlan(validPlan)).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> planService.createPlan(validRequestDTO, userId, goalId))
                .isInstanceOf(PlanException.class)
                .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_CREATION_FAILED);
    }
}