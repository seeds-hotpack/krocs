package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.converter.GoalConverter;
import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.dto.request.GoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.GoalUpdateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalCreateResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.GoalRepositoryFacade;
import com.hotpack.krocs.global.common.entity.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

    @Mock
    private GoalRepositoryFacade goalRepositoryFacade;

    @Mock
    private GoalConverter goalConvertor;

    @InjectMocks
    private GoalServiceImpl goalService;

    private GoalCreateRequestDTO validRequestDTO;
    private Goal validGoal;
    private GoalCreateResponseDTO validResponseDTO;

    @BeforeEach
    void setUp() {
        validRequestDTO = GoalCreateRequestDTO.builder()
                .title("테스트 목표")
                .priority(Priority.HIGH)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(365))
                .duration(365)
                .build();

        validGoal = Goal.builder()
                .goalId(1L)
                .title("테스트 목표")
                .priority(Priority.HIGH)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(365))
                .duration(365)
                .isCompleted(false)
                .build();

        validResponseDTO = GoalCreateResponseDTO.builder()
                .goalId(1L)
                .title("테스트 목표")
                .priority(Priority.HIGH)
                .startDate(LocalDate.now().plusDays(1))
                .endDate(LocalDate.now().plusDays(365))
                .duration(365)
                .completed(false)
                .completionPercentage(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ========== CREATE 테스트 ==========

    @Test
    @DisplayName("대목표 생성 성공 테스트")
    void createGoal_Success() {
        // given
        when(goalConvertor.toEntity(validRequestDTO)).thenReturn(validGoal);
        when(goalRepositoryFacade.saveGoal(validGoal)).thenReturn(validGoal);
        when(goalConvertor.toCreateResponseDTO(validGoal)).thenReturn(validResponseDTO);

        // when
        GoalCreateResponseDTO result = goalService.createGoal(validRequestDTO, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("테스트 목표");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    @DisplayName("대목표 생성 - 제목이 비어있는 경우")
    void createGoal_EmptyTitle() {
        // given
        GoalCreateRequestDTO invalidRequest = GoalCreateRequestDTO.builder()
                .title("")
                .duration(30)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_TITLE_EMPTY);
    }

    @Test
    @DisplayName("대목표 생성 - 제목이 null인 경우")
    void createGoal_NullTitle() {
        // given
        GoalCreateRequestDTO invalidRequest = GoalCreateRequestDTO.builder()
                .title(null)
                .duration(30)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_TITLE_EMPTY);
    }

    @Test
    @DisplayName("대목표 생성 - 제목이 공백인 경우")
    void createGoal_BlankTitle() {
        // given
        GoalCreateRequestDTO invalidRequest = GoalCreateRequestDTO.builder()
                .title("   ")
                .duration(30)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_TITLE_EMPTY);
    }

    @Test
    @DisplayName("대목표 생성 - 기간이 null인 경우")
    void createGoal_NullDuration() {
        // given
        GoalCreateRequestDTO invalidRequest = GoalCreateRequestDTO.builder()
                .title("테스트 목표")
                .duration(null)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_DURATION_INVALID);
    }

    @Test
    @DisplayName("대목표 생성 - 기간이 0인 경우")
    void createGoal_ZeroDuration() {
        // given
        GoalCreateRequestDTO invalidRequest = GoalCreateRequestDTO.builder()
                .title("테스트 목표")
                .duration(0)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_DURATION_INVALID);
    }

    @Test
    @DisplayName("대목표 생성 - 기간이 음수인 경우")
    void createGoal_NegativeDuration() {
        // given
        GoalCreateRequestDTO invalidRequest = GoalCreateRequestDTO.builder()
                .title("테스트 목표")
                .duration(-1)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_DURATION_INVALID);
    }

    @Test
    @DisplayName("대목표 생성 - 날짜 범위가 유효하지 않은 경우")
    void createGoal_InvalidDateRange() {
        // given
        GoalCreateRequestDTO invalidRequest = GoalCreateRequestDTO.builder()
                .title("테스트 목표")
                .startDate(LocalDate.of(2024, 12, 31))
                .endDate(LocalDate.of(2024, 1, 1))
                .duration(30)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.INVALID_GOAL_DATE_RANGE);
    }

    @Test
    @DisplayName("대목표 생성 - Repository에서 예외 발생")
    void createGoal_RepositoryException() {
        // given
        when(goalConvertor.toEntity((GoalCreateRequestDTO) any())).thenReturn(validGoal);
        when(goalRepositoryFacade.saveGoal(any())).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(validRequestDTO, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_CREATION_FAILED);
    }

    @Test
    @DisplayName("대목표 생성 - Convertor에서 예외 발생")
    void createGoal_ConvertorException() {
        // given
        when(goalConvertor.toEntity((GoalCreateRequestDTO) any())).thenThrow(new RuntimeException("변환 오류"));

        // when & then
        assertThatThrownBy(() -> goalService.createGoal(validRequestDTO, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_CREATION_FAILED);
    }

    @Test
    @DisplayName("대목표 생성 - 최소 필수 데이터만으로 성공")
    void createGoal_MinimalData() {
        // given
        GoalCreateRequestDTO minimalRequest = GoalCreateRequestDTO.builder()
                .title("최소 목표")
                .duration(1)
                .build();

        Goal minimalGoal = Goal.builder()
                .goalId(1L)
                .title("최소 목표")
                .priority(Priority.MEDIUM)
                .duration(1)
                .isCompleted(false)
                .build();

        GoalCreateResponseDTO minimalResponse = GoalCreateResponseDTO.builder()
                .goalId(1L)
                .title("최소 목표")
                .priority(Priority.MEDIUM)
                .duration(1)
                .completed(false)
                .completionPercentage(0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(goalConvertor.toEntity(minimalRequest)).thenReturn(minimalGoal);
        when(goalRepositoryFacade.saveGoal(minimalGoal)).thenReturn(minimalGoal);
        when(goalConvertor.toCreateResponseDTO(minimalGoal)).thenReturn(minimalResponse);

        // when
        GoalCreateResponseDTO result = goalService.createGoal(minimalRequest, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("최소 목표");
        assertThat(result.getPriority()).isEqualTo(Priority.MEDIUM);
        assertThat(result.getDuration()).isEqualTo(1);
    }

    // ========== UPDATE 테스트 ==========

    @Test
    @DisplayName("목표 수정 성공 - 제목만 수정")
    void updateGoalById_Success_TitleOnly() {
        // given
        Long goalId = 1L;
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .title("수정된 제목")
                .build();

        Goal existingGoal = Goal.builder()
                .goalId(1L)
                .title("기존 제목")
                .priority(Priority.HIGH)
                .duration(30)
                .isCompleted(false)
                .build();

        Goal updatedGoal = Goal.builder()
                .goalId(1L)
                .title("수정된 제목")
                .priority(Priority.HIGH)
                .duration(30)
                .isCompleted(false)
                .build();

        GoalResponseDTO expectedResponse = GoalResponseDTO.builder()
                .goalId(1L)
                .title("수정된 제목")
                .priority(Priority.HIGH)
                .duration(30)
                .isCompleted(false)
                .build();

        when(goalRepositoryFacade.findById(goalId)).thenReturn(existingGoal);
        when(goalRepositoryFacade.updateGoal(any(Goal.class))).thenReturn(updatedGoal);
        when(goalConvertor.toGoalResponseDTO(updatedGoal)).thenReturn(expectedResponse);

        // when
        GoalResponseDTO result = goalService.updateGoalById(goalId, updateRequest, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.getDuration()).isEqualTo(30);
    }

    @Test
    @DisplayName("목표 수정 성공 - 여러 필드 수정")
    void updateGoalById_Success_MultipleFields() {
        // given
        Long goalId = 1L;
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .title("수정된 제목")
                .priority(Priority.LOW)
                .startDate(LocalDate.of(2025, 8, 1))
                .endDate(LocalDate.of(2025, 8, 31))
                .duration(31)
                .build();

        Goal existingGoal = Goal.builder()
                .goalId(1L)
                .title("기존 제목")
                .priority(Priority.HIGH)
                .startDate(LocalDate.of(2025, 7, 1))
                .endDate(LocalDate.of(2025, 7, 31))
                .duration(30)
                .isCompleted(false)
                .build();

        Goal updatedGoal = Goal.builder()
                .goalId(1L)
                .title("수정된 제목")
                .priority(Priority.LOW)
                .startDate(LocalDate.of(2025, 8, 1))
                .endDate(LocalDate.of(2025, 8, 31))
                .duration(31)
                .isCompleted(false)
                .build();

        GoalResponseDTO expectedResponse = GoalResponseDTO.builder()
                .goalId(1L)
                .title("수정된 제목")
                .priority(Priority.LOW)
                .startDate(LocalDate.of(2025, 8, 1))
                .endDate(LocalDate.of(2025, 8, 31))
                .duration(31)
                .isCompleted(false)
                .build();

        when(goalRepositoryFacade.findById(goalId)).thenReturn(existingGoal);
        when(goalRepositoryFacade.updateGoal(any(Goal.class))).thenReturn(updatedGoal);
        when(goalConvertor.toGoalResponseDTO(updatedGoal)).thenReturn(expectedResponse);

        // when
        GoalResponseDTO result = goalService.updateGoalById(goalId, updateRequest, 1L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("수정된 제목");
        assertThat(result.getPriority()).isEqualTo(Priority.LOW);
        assertThat(result.getStartDate()).isEqualTo(LocalDate.of(2025, 8, 1));
        assertThat(result.getEndDate()).isEqualTo(LocalDate.of(2025, 8, 31));
        assertThat(result.getDuration()).isEqualTo(31);
    }

    @Test
    @DisplayName("목표 수정 실패 - 존재하지 않는 goalId")
    void updateGoalById_Fail_GoalNotFound() {
        // given
        Long goalId = 999L;
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .title("수정된 제목")
                .build();

        when(goalRepositoryFacade.findById(goalId)).thenThrow(new GoalException(GoalExceptionType.GOAL_NOT_FOUND));

        // when & then
        assertThatThrownBy(() -> goalService.updateGoalById(goalId, updateRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_NOT_FOUND);
    }

    @Test
    @DisplayName("목표 수정 실패 - 유효하지 않은 제목 (빈 문자열)")
    void updateGoalById_Fail_InvalidTitle() {
        // given
        Long goalId = 1L;
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .title("")
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.updateGoalById(goalId, updateRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_TITLE_EMPTY);
    }

    @Test
    @DisplayName("목표 수정 실패 - 유효하지 않은 기간 (0)")
    void updateGoalById_Fail_InvalidDuration() {
        // given
        Long goalId = 1L;
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .duration(0)
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.updateGoalById(goalId, updateRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_DURATION_INVALID);
    }

    @Test
    @DisplayName("목표 수정 실패 - 유효하지 않은 날짜 범위")
    void updateGoalById_Fail_InvalidDateRange() {
        // given
        Long goalId = 1L;
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .startDate(LocalDate.of(2025, 12, 31))
                .endDate(LocalDate.of(2025, 1, 1))
                .build();

        // when & then
        assertThatThrownBy(() -> goalService.updateGoalById(goalId, updateRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.INVALID_GOAL_DATE_RANGE);
    }

    @Test
    @DisplayName("목표 수정 - Repository에서 예외 발생")
    void updateGoalById_RepositoryException() {
        // given
        Long goalId = 1L;
        GoalUpdateRequestDTO updateRequest = GoalUpdateRequestDTO.builder()
                .title("수정된 제목")
                .build();

        Goal existingGoal = Goal.builder()
                .goalId(1L)
                .title("기존 제목")
                .build();

        when(goalRepositoryFacade.findById(goalId)).thenReturn(existingGoal);
        when(goalRepositoryFacade.updateGoal(any())).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> goalService.updateGoalById(goalId, updateRequest, 1L))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_CREATION_FAILED);
    }

    // ========== DELETE 테스트 ==========

    @Test
    @DisplayName("목표 삭제 성공")
    void deleteGoal_Success() {
        // given
        Long goalId = 1L;
        Long userId = 1L;

        when(goalRepositoryFacade.existsById(goalId)).thenReturn(true);
        doNothing().when(goalRepositoryFacade).deleteGoal(goalId);

        // when & then
        assertThatCode(() -> goalService.deleteGoal(userId, goalId))
                .doesNotThrowAnyException();

        verify(goalRepositoryFacade).existsById(goalId);
        verify(goalRepositoryFacade).deleteGoal(goalId);
    }

    @Test
    @DisplayName("목표 삭제 실패 - 존재하지 않는 goalId")
    void deleteGoal_Fail_GoalNotFound() {
        // given
        Long goalId = 999L;
        Long userId = 1L;

        when(goalRepositoryFacade.existsById(goalId)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> goalService.deleteGoal(userId, goalId))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_NOT_FOUND);

        verify(goalRepositoryFacade).existsById(goalId);
        verify(goalRepositoryFacade, never()).deleteGoal(any());
    }

    @Test
    @DisplayName("목표 삭제 - Repository에서 예외 발생")
    void deleteGoal_RepositoryException() {
        // given
        Long goalId = 1L;
        Long userId = 1L;

        when(goalRepositoryFacade.existsById(goalId)).thenReturn(true);
        doThrow(new RuntimeException("데이터베이스 오류")).when(goalRepositoryFacade).deleteGoal(goalId);

        // when & then
        assertThatThrownBy(() -> goalService.deleteGoal(userId, goalId))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_CREATION_FAILED);
    }

    // ========== GET 테스트 ==========

    @Test
    @DisplayName("단일 목표 조회 성공")
    void getGoalByGoalId_Success() {
        // given
        Long goalId = 1L;
        Long userId = 1L;
        Goal existingGoal = Goal.builder()
                .goalId(1L)
                .title("조회할 목표")
                .priority(Priority.HIGH)
                .duration(30)
                .isCompleted(false)
                .build();

        GoalResponseDTO expectedResponse = GoalResponseDTO.builder()
                .goalId(1L)
                .title("조회할 목표")
                .priority(Priority.HIGH)
                .duration(30)
                .isCompleted(false)
                .build();

        when(goalRepositoryFacade.findGoalByGoalId(goalId)).thenReturn(existingGoal);
        when(goalConvertor.toGoalResponseDTO(existingGoal)).thenReturn(expectedResponse);

        // when
        GoalResponseDTO result = goalService.getGoalByGoalId(userId, goalId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getGoalId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("조회할 목표");
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(result.getDuration()).isEqualTo(30);
    }

    @Test
    @DisplayName("단일 목표 조회 실패 - null goalId")
    void getGoalByGoalId_Fail_NullGoalId() {
        // given
        Long goalId = null;
        Long userId = 1L;

        // when & then
        assertThatThrownBy(() -> goalService.getGoalByGoalId(userId, goalId))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_NOT_FOUND);
    }

    @Test
    @DisplayName("단일 목표 조회 실패 - 존재하지 않는 goalId")
    void getGoalByGoalId_Fail_GoalNotFound() {
        // given
        Long goalId = 999L;
        Long userId = 1L;

        when(goalRepositoryFacade.findGoalByGoalId(goalId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() -> goalService.getGoalByGoalId(userId, goalId))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_NOT_FOUND);
    }

    @Test
    @DisplayName("단일 목표 조회 - Repository에서 예외 발생")
    void getGoalByGoalId_RepositoryException() {
        // given
        Long goalId = 1L;
        Long userId = 1L;

        when(goalRepositoryFacade.findGoalByGoalId(goalId)).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> goalService.getGoalByGoalId(userId, goalId))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_FOUND_FAILED);
    }

    @Test
    @DisplayName("사용자별 목표 목록 조회 성공 - 날짜 필터 없음")
    void getGoalByUser_Success_NoDateFilter() {
        // given
        Long userId = 1L;
        LocalDate date = null;

        List<Goal> goalList = Arrays.asList(
                Goal.builder().goalId(1L).title("목표1").build(),
                Goal.builder().goalId(2L).title("목표2").build()
        );

        List<GoalResponseDTO> expectedResponse = Arrays.asList(
                GoalResponseDTO.builder().goalId(1L).title("목표1").build(),
                GoalResponseDTO.builder().goalId(2L).title("목표2").build()
        );

        when(goalRepositoryFacade.findAllGoals()).thenReturn(goalList);
        when(goalConvertor.toGoalResponseDTO(goalList)).thenReturn(expectedResponse);

        // when
        List<GoalResponseDTO> result = goalService.getGoalByUser(userId, date);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTitle()).isEqualTo("목표1");
        assertThat(result.get(1).getTitle()).isEqualTo("목표2");
    }

    @Test
    @DisplayName("사용자별 목표 목록 조회 성공 - 날짜 필터 있음")
    void getGoalByUser_Success_WithDateFilter() {
        // given
        Long userId = 1L;
        LocalDate date = LocalDate.of(2025, 7, 25);


        List<Goal> goalList = Arrays.asList(
                Goal.builder().goalId(1L).title("현재 진행 목표").build()
        );

        List<GoalResponseDTO> expectedResponse = Arrays.asList(
                GoalResponseDTO.builder().goalId(1L).title("현재 진행 목표").build()
        );

        when(goalRepositoryFacade.findGoalByDate(date)).thenReturn(goalList);
        when(goalConvertor.toGoalResponseDTO(goalList)).thenReturn(expectedResponse);

        // when
        List<GoalResponseDTO> result = goalService.getGoalByUser(userId, date);

        // then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("현재 진행 목표");
    }

    @Test
    @DisplayName("사용자별 목표 목록 조회 성공 - 빈 결과")
    void getGoalByUser_Success_EmptyResult() {
        // given
        Long userId = 1L;
        LocalDate date = null;
        List<Goal> emptyGoalList = Collections.emptyList();
        List<GoalResponseDTO> emptyResponse = Collections.emptyList();

        when(goalRepositoryFacade.findAllGoals()).thenReturn(emptyGoalList);
        when(goalConvertor.toGoalResponseDTO(emptyGoalList)).thenReturn(emptyResponse);

        // when
        List<GoalResponseDTO> result = goalService.getGoalByUser(userId, date);

        // then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("사용자별 목표 목록 조회 - Repository에서 예외 발생")
    void getGoalByUser_RepositoryException() {
        // given
        Long userId = 1L;
        LocalDate date = null;

        when(goalRepositoryFacade.findAllGoals()).thenThrow(new RuntimeException("데이터베이스 오류"));

        // when & then
        assertThatThrownBy(() -> goalService.getGoalByUser(userId, date))
                .isInstanceOf(GoalException.class)
                .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_FOUND_FAILED);
    }
} 