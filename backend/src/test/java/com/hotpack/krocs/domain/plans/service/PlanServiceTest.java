package com.hotpack.krocs.domain.plans.service;

import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.facade.SubGoalRepositoryFacade;
import com.hotpack.krocs.domain.plans.converter.PlanConverter;
import com.hotpack.krocs.domain.plans.domain.Plan;
import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.request.PlanUpdateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanListResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanResponseDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import com.hotpack.krocs.domain.plans.facade.PlanRepositoryFacade;
import com.hotpack.krocs.domain.plans.validator.PlanValidator;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
    private List<Plan> validPlanList;
    private List<PlanResponseDTO> validPlanResponseList;
    private PlanListResponseDTO validPlanListResponseDTO;

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

        Plan plan2 = Plan.builder()
            .planId(2L)
            .goal(null)
            .subGoal(null)
            .title("독립 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 2, 14, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 2, 15, 0))
            .allDay(false)
            .isCompleted(false)
            .build();

        validPlanList = Arrays.asList(validPlan, plan2);

        PlanResponseDTO responseDTO2 = PlanResponseDTO.builder()
            .planId(2L)
            .goalId(null)
            .subGoalId(null)
            .title("독립 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 2, 14, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 2, 15, 0))
            .allDay(false)
            .isCompleted(false)
            .build();

        validPlanResponseList = Arrays.asList(validResponseDTO, responseDTO2);

        validPlanListResponseDTO = PlanListResponseDTO.builder()
            .plans(validPlanResponseList)
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

    // ========== GET 테스트 ==========

    @Test
    @DisplayName("모든 일정 조회 성공")
    void getAllPlans_Success() {
        // given
        Long userId = 1L;

        when(planRepositoryFacade.findAllPlans()).thenReturn(validPlanList);
        when(planConverter.toListPlanResponseDTO(validPlanList)).thenReturn(validPlanResponseList);

        // when
        PlanListResponseDTO result = planService.getAllPlans(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPlans()).isNotNull();
        assertThat(result.getPlans()).hasSize(2);

        // 첫 번째 일정 (Goal/SubGoal 연결된 일정)
        PlanResponseDTO firstPlan = result.getPlans().getFirst();
        assertThat(firstPlan.getPlanId()).isEqualTo(1L);
        assertThat(firstPlan.getTitle()).isEqualTo("테스트 일정");
        assertThat(firstPlan.getGoalId()).isEqualTo(1L);
        assertThat(firstPlan.getSubGoalId()).isEqualTo(1L);
        assertThat(firstPlan.getIsCompleted()).isFalse();

        // 두 번째 일정 (독립 일정)
        PlanResponseDTO secondPlan = result.getPlans().get(1);
        assertThat(secondPlan.getPlanId()).isEqualTo(2L);
        assertThat(secondPlan.getTitle()).isEqualTo("독립 일정");
        assertThat(secondPlan.getGoalId()).isNull();
        assertThat(secondPlan.getSubGoalId()).isNull();
        assertThat(secondPlan.getIsCompleted()).isFalse();
    }

    @Test
    @DisplayName("모든 일정 조회 성공 - 빈 리스트")
    void getAllPlans_Success_EmptyList() {
        // given
        Long userId = 1L;
        List<Plan> emptyPlanList = Collections.emptyList();
        List<PlanResponseDTO> emptyResponseList = Collections.emptyList();

        when(planRepositoryFacade.findAllPlans()).thenReturn(emptyPlanList);
        when(planConverter.toListPlanResponseDTO(emptyPlanList)).thenReturn(emptyResponseList);

        // when
        PlanListResponseDTO result = planService.getAllPlans(userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPlans()).isNotNull();
        assertThat(result.getPlans()).isEmpty();
    }

    @Test
    @DisplayName("모든 일정 조회 실패 - Repository에서 예외 발생")
    void getAllPlans_Fail_RepositoryException() {
        // given
        Long userId = 1L;

        when(planRepositoryFacade.findAllPlans()).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> planService.getAllPlans(userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_FOUND_FAILED);

        verify(planRepositoryFacade).findAllPlans();
        verify(planConverter, never()).toListPlanResponseDTO(any());
    }

    @Test
    @DisplayName("모든 일정 조회 실패 - Converter에서 예외 발생")
    void getAllPlans_Fail_ConverterException() {
        // given
        Long userId = 1L;

        when(planRepositoryFacade.findAllPlans()).thenReturn(validPlanList);
        when(planConverter.toListPlanResponseDTO(validPlanList)).thenThrow(new RuntimeException("변환 오류"));

        // when & then
        assertThatThrownBy(() -> planService.getAllPlans(userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_FOUND_FAILED);
    }

    @Test
    @DisplayName("특정 일정 조회 성공 - Goal/SubGoal 연결된 일정")
    void getPlanById_Success_WithGoalAndSubGoal() {
        // given
        Long planId = 1L;
        Long userId = 1L;

        doNothing().when(planValidator).validateGetPlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.getPlanById(planId, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPlanId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 일정");
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getSubGoalId()).isEqualTo(1L);
        assertThat(result.getAllDay()).isFalse();
        assertThat(result.getIsCompleted()).isFalse();
        verify(planValidator).validateGetPlan(planId);
    }

    @Test
    @DisplayName("특정 일정 조회 성공 - 독립 일정 (Goal/SubGoal 없음)")
    void getPlanById_Success_IndependentPlan() {
        // given
        Long planId = 2L;
        Long userId = 1L;

        Plan independentPlan = Plan.builder()
            .planId(2L)
            .goal(null)
            .subGoal(null)
            .title("독립 일정")
            .allDay(true)
            .isCompleted(false)
            .build();

        PlanResponseDTO independentResponse = PlanResponseDTO.builder()
            .planId(2L)
            .goalId(null)
            .subGoalId(null)
            .title("독립 일정")
            .allDay(true)
            .isCompleted(false)
            .build();

        doNothing().when(planValidator).validateGetPlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(independentPlan);
        when(planConverter.toEntity(independentPlan)).thenReturn(independentResponse);

        // when
        PlanResponseDTO result = planService.getPlanById(planId, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getPlanId()).isEqualTo(2L);
        assertThat(result.getTitle()).isEqualTo("독립 일정");
        assertThat(result.getGoalId()).isNull();
        assertThat(result.getSubGoalId()).isNull();
        assertThat(result.getAllDay()).isTrue();
        verify(planValidator).validateGetPlan(planId);
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - 존재하지 않는 planId")
    void getPlanById_Fail_PlanNotFound() {
        // given
        Long planId = 999L;
        Long userId = 1L;

        doNothing().when(planValidator).validateGetPlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> planService.getPlanById(planId, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_NOT_FOUND);

        verify(planRepositoryFacade).findPlanById(planId);
        verify(planConverter, never()).toEntity(any(Plan.class));
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - Repository에서 예외 발생")
    void getPlanById_Fail_RepositoryException() {
        // given
        Long planId = 1L;
        Long userId = 1L;

        doNothing().when(planValidator).validateGetPlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> planService.getPlanById(planId, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_FOUND_FAILED);
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - Converter에서 예외 발생")
    void getPlanById_Fail_ConverterException() {
        // given
        Long planId = 1L;
        Long userId = 1L;

        doNothing().when(planValidator).validateGetPlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(planConverter.toEntity(validPlan)).thenThrow(new RuntimeException("변환 오류"));

        // when & then
        assertThatThrownBy(() -> planService.getPlanById(planId, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_FOUND_FAILED);
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - null planId")
    void getPlanById_Fail_NullPlanId() {
        // given
        Long planId = null;
        Long userId = 1L;

        doThrow(new PlanException(PlanExceptionType.PLAN_INVALID_PLAN_ID))
            .when(planValidator).validateGetPlan(planId);

        // when & then
        assertThatThrownBy(() -> planService.getPlanById(planId, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_INVALID_PLAN_ID);

        verify(planValidator).validateGetPlan(planId);
        verify(planRepositoryFacade, never()).findPlanById(any());
        verify(planConverter, never()).toEntity(any(Plan.class));
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - 음수 planId")
    void getPlanById_Fail_NegativePlanId() {
        // given
        Long planId = -1L;
        Long userId = 1L;

        doThrow(new PlanException(PlanExceptionType.PLAN_INVALID_PLAN_ID))
            .when(planValidator).validateGetPlan(planId);

        // when & then
        assertThatThrownBy(() -> planService.getPlanById(planId, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_INVALID_PLAN_ID);

        verify(planValidator).validateGetPlan(planId);
        verify(planRepositoryFacade, never()).findPlanById(any());
        verify(planConverter, never()).toEntity(any(Plan.class));
    }

    @Test
    @DisplayName("특정 일정 조회 실패 - 0인 planId")
    void getPlanById_Fail_ZeroPlanId() {
        // given
        Long planId = 0L;
        Long userId = 1L;

        doThrow(new PlanException(PlanExceptionType.PLAN_INVALID_PLAN_ID))
            .when(planValidator).validateGetPlan(planId);

        // when & then
        assertThatThrownBy(() -> planService.getPlanById(planId, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_INVALID_PLAN_ID);

        verify(planValidator).validateGetPlan(planId);
        verify(planRepositoryFacade, never()).findPlanById(any());
        verify(planConverter, never()).toEntity(any(Plan.class));
    }

    // ========== UPDATE 테스트 ==========

    @Test
    @DisplayName("일정 수정 성공 - 제목만 수정")
    void updatePlanById_Success_TitleOnly() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .title("수정된 제목")
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doNothing().when(planValidator).validateTitle("수정된 제목");
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class)); // 아무 DTO나 반환
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(validResponseDTO);

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planValidator).validateTitle("수정된 제목");
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            validPlan.getAllDay(),
            validPlan.getStartDateTime(),
            validPlan.getEndDateTime()
        );
    }

    @Test
    @DisplayName("일정 수정 성공 - 시간만 수정")
    void updatePlanById_Success_DateTimeOnly() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        LocalDateTime newStartTime = LocalDateTime.of(2025, 8, 2, 14, 0);
        LocalDateTime newEndTime = LocalDateTime.of(2025, 8, 2, 16, 0);

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .startDateTime(newStartTime)
            .endDateTime(newEndTime)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doNothing().when(planValidator).validateDateRange(newStartTime, newEndTime);
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planValidator).validateDateRange(newStartTime, newEndTime);
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            validPlan.getAllDay(),
            newStartTime,
            newEndTime
        );
    }

    @Test
    @DisplayName("일정 수정 성공 - allDay를 true로 변경 (시간 정규화)")
    void updatePlanById_Success_ChangeToAllDay() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        LocalDateTime normalizedStartTime = LocalDateTime.of(2025, 8, 1, 0, 0);
        LocalDateTime normalizedEndTime = LocalDateTime.of(2025, 8, 1, 23, 59, 59);

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .allDay(true)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doNothing().when(planValidator).validateAllDayDateTime(true, normalizedStartTime, normalizedEndTime);
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planValidator).validateAllDayDateTime(true, normalizedStartTime, normalizedEndTime);
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            true,
            normalizedStartTime,
            normalizedEndTime
        );
    }

    @Test
    @DisplayName("일정 수정 성공 - allDay를 false로 변경")
    void updatePlanById_Success_ChangeToNonAllDay() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        Plan allDayPlan = Plan.builder()
            .planId(1L)
            .goal(validGoal)
            .subGoal(validSubGoal)
            .title("하루 종일 일정")
            .startDateTime(LocalDateTime.of(2025, 8, 1, 0, 0))
            .endDateTime(LocalDateTime.of(2025, 8, 1, 23, 59, 59))
            .allDay(true)
            .isCompleted(false)
            .build();

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .allDay(false)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(allDayPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doNothing().when(planValidator).validateAllDayDateTime(false, allDayPlan.getStartDateTime(), allDayPlan.getEndDateTime());
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(allDayPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planValidator).validateAllDayDateTime(false, allDayPlan.getStartDateTime(), allDayPlan.getEndDateTime());
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            false,
            allDayPlan.getStartDateTime(),
            allDayPlan.getEndDateTime()
        );
    }

    @Test
    @DisplayName("일정 수정 성공 - 완료 상태로 변경 (completedAt 설정)")
    void updatePlanById_Success_ChangeToCompleted() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .isCompleted(true)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            validPlan.getAllDay(),
            validPlan.getStartDateTime(),
            validPlan.getEndDateTime()
        );
    }

    @Test
    @DisplayName("일정 수정 성공 - 완료 상태 해제 (completedAt null)")
    void updatePlanById_Success_ChangeToNotCompleted() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        Plan completedPlan = Plan.builder()
            .planId(1L)
            .goal(validGoal)
            .subGoal(validSubGoal)
            .title("완료된 일정")
            .startDateTime(validPlan.getStartDateTime())
            .endDateTime(validPlan.getEndDateTime())
            .allDay(false)
            .isCompleted(true)
            .completedAt(LocalDateTime.now())
            .build();

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .isCompleted(false)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(completedPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(completedPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            completedPlan.getAllDay(),
            completedPlan.getStartDateTime(),
            completedPlan.getEndDateTime()
        );
    }

    @Test
    @DisplayName("일정 수정 성공 - 모든 필드 동시 수정")
    void updatePlanById_Success_AllFieldsUpdate() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        LocalDateTime newStartTime = LocalDateTime.of(2025, 8, 3, 10, 0);
        LocalDateTime newEndTime = LocalDateTime.of(2025, 8, 3, 12, 0);
        LocalDateTime normalizedStartTime = LocalDateTime.of(2025, 8, 3, 0, 0);
        LocalDateTime normalizedEndTime = LocalDateTime.of(2025, 8, 3, 23, 59, 59);

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .title("완전히 새로운 제목")
            .startDateTime(newStartTime)
            .endDateTime(newEndTime)
            .allDay(true)
            .isCompleted(true)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        doNothing().when(planValidator).validateTitle("완전히 새로운 제목");
        doNothing().when(planValidator).validateDateRange(newStartTime, newEndTime);
        doNothing().when(planValidator).validateAllDayDateTime(true, normalizedStartTime, normalizedEndTime);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planValidator).validateTitle("완전히 새로운 제목");
        verify(planValidator).validateDateRange(newStartTime, newEndTime);
        verify(planValidator).validateAllDayDateTime(true, normalizedStartTime, normalizedEndTime);
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            true,
            normalizedStartTime,
            normalizedEndTime
        );
    }

    @Test
    @DisplayName("일정 수정 실패 - 존재하지 않는 planId")
    void updatePlanById_Fail_PlanNotFound() {
        // given
        Long planId = 999L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .title("수정된 제목")
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> planService.updatePlanById(planId, subGoalId, updateRequest, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_NOT_FOUND);

        verify(planRepositoryFacade).findPlanById(planId);
        verify(planValidator, never()).validateTitle(any());
        verify(planConverter, never()).toUpdatePlanRequestDTO(any(), any(), any(), any());
    }

    @Test
    @DisplayName("일정 수정 실패 - 제목 유효성 검사 실패")
    void updatePlanById_Fail_InvalidTitle() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .title("") // 빈 제목
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doThrow(new PlanException(PlanExceptionType.PLAN_TITLE_EMPTY))
            .when(planValidator).validateTitle("");

        // when & then
        assertThatThrownBy(() -> planService.updatePlanById(planId, subGoalId, updateRequest, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_TITLE_EMPTY);

        verify(planValidator).validateTitle("");
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planConverter, never()).toUpdatePlanRequestDTO(any(), any(), any(), any());
    }

    @Test
    @DisplayName("일정 수정 실패 - 날짜 범위 유효성 검사 실패")
    void updatePlanById_Fail_InvalidDateRange() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        LocalDateTime invalidStartTime = LocalDateTime.of(2025, 8, 1, 15, 0);
        LocalDateTime invalidEndTime = LocalDateTime.of(2025, 8, 1, 10, 0);

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .startDateTime(invalidStartTime)
            .endDateTime(invalidEndTime)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doThrow(new PlanException(PlanExceptionType.INVALID_PLAN_DATE_RANGE))
            .when(planValidator).validateDateRange(invalidStartTime, invalidEndTime);

        // when & then
        assertThatThrownBy(() -> planService.updatePlanById(planId, subGoalId, updateRequest, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.INVALID_PLAN_DATE_RANGE);

        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planValidator).validateDateRange(invalidStartTime, invalidEndTime);
        verify(planConverter, never()).toUpdatePlanRequestDTO(any(), any(), any(), any());
    }

    @Test
    @DisplayName("일정 수정 실패 - allDay 유효성 검사 실패")
    void updatePlanById_Fail_AllDayValidationError() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .allDay(true)
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doThrow(new PlanException(PlanExceptionType.INVALID_PLAN_DATE_RANGE))
            .when(planValidator).validateAllDayDateTime(any(), any(), any());

        // when & then
        assertThatThrownBy(() -> planService.updatePlanById(planId, subGoalId, updateRequest, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.INVALID_PLAN_DATE_RANGE);

        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(subGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(subGoalId);
        verify(planValidator).validateAllDayDateTime(any(), any(), any());
        verify(planConverter, never()).toUpdatePlanRequestDTO(any(), any(), any(), any());
    }

    @Test
    @DisplayName("일정 수정 실패 - Repository에서 예외 발생")
    void updatePlanById_Fail_RepositoryException() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .title("수정된 제목")
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> planService.updatePlanById(planId, subGoalId, updateRequest, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_UPDATE_FAILED);

        verify(planRepositoryFacade).findPlanById(planId);
        verify(planConverter, never()).toUpdatePlanRequestDTO(any(), any(), any(), any());
    }

    @Test
    @DisplayName("일정 수정 실패 - Converter에서 예외 발생")
    void updatePlanById_Fail_ConverterException() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .title("수정된 제목")
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        doNothing().when(planValidator).validateTitle("수정된 제목");
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenThrow(new RuntimeException("변환 오류"));

        // when & then
        assertThatThrownBy(() -> planService.updatePlanById(planId, subGoalId, updateRequest, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_UPDATE_FAILED);
    }

    @Test
    @DisplayName("일정 수정 성공 - 빈 request (아무것도 변경하지 않음)")
    void updatePlanById_Success_EmptyRequest() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long subGoalId = 1L;

        PlanUpdateRequestDTO emptyRequest = PlanUpdateRequestDTO.builder().build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId)).thenReturn(validSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(subGoalId)).thenReturn(validGoal);
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, subGoalId, emptyRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(planValidator, never()).validateTitle(any());
        verify(planValidator, never()).validateDateRange(any(), any());
        verify(planValidator, never()).validateAllDayDateTime(any(), any(), any());
        verify(planConverter).toUpdatePlanRequestDTO(
            emptyRequest,
            validPlan.getAllDay(),
            validPlan.getStartDateTime(),
            validPlan.getEndDateTime()
        );
    }

    @Test
    @DisplayName("일정 수정 성공 - subGoalId만 변경")
    void updatePlanById_Success_SubGoalIdOnly() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long newSubGoalId = 2L;

        // 새로운 SubGoal과 Goal 생성
        SubGoal newSubGoal = SubGoal.builder()
            .subGoalId(2L)
            .goal(validGoal)
            .title("새로운 서브목표")
            .isCompleted(false)
            .build();

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .build(); // 빈 요청 (subGoalId만 변경)

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(newSubGoalId)).thenReturn(newSubGoal);
        when(subGoalRepositoryFacade.findGoalBySubGoalId(newSubGoalId)).thenReturn(validGoal);
        when(planConverter.toUpdatePlanRequestDTO(any(), any(), any(), any()))
            .thenReturn(mock(PlanUpdateRequestDTO.class));
        when(planConverter.toEntity(validPlan)).thenReturn(validResponseDTO);

        // when
        PlanResponseDTO result = planService.updatePlanById(planId, newSubGoalId, updateRequest, userId);

        // then
        assertThat(result).isNotNull();

        verify(planValidator).validateUpdatePlan(planId);
        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(newSubGoalId);
        verify(subGoalRepositoryFacade).findGoalBySubGoalId(newSubGoalId);
        verify(planConverter).toUpdatePlanRequestDTO(
            updateRequest,
            validPlan.getAllDay(),
            validPlan.getStartDateTime(),
            validPlan.getEndDateTime()
        );
    }

    @Test
    @DisplayName("일정 수정 실패 - 존재하지 않는 subGoalId")
    void updatePlanById_Fail_SubGoalNotFound() {
        // given
        Long planId = 1L;
        Long userId = 1L;
        Long invalidSubGoalId = 999L;

        PlanUpdateRequestDTO updateRequest = PlanUpdateRequestDTO.builder()
            .title("수정된 제목")
            .build();

        doNothing().when(planValidator).validateUpdatePlan(planId);
        when(planRepositoryFacade.findPlanById(planId)).thenReturn(validPlan);
        when(subGoalRepositoryFacade.findSubGoalBySubGoalId(invalidSubGoalId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> planService.updatePlanById(planId, invalidSubGoalId, updateRequest, userId))
            .isInstanceOf(PlanException.class)
            .hasFieldOrPropertyWithValue("planExceptionType", PlanExceptionType.PLAN_SUB_GOAL_NOT_FOUND);

        verify(subGoalRepositoryFacade).findSubGoalBySubGoalId(invalidSubGoalId);
        verify(subGoalRepositoryFacade, never()).findGoalBySubGoalId(any());
        verify(planConverter, never()).toUpdatePlanRequestDTO(any(), any(), any(), any());
    }
}