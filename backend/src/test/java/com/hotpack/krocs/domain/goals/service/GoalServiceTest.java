package com.hotpack.krocs.domain.goals.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hotpack.krocs.domain.goals.convertor.GoalConvertor;
import com.hotpack.krocs.domain.goals.domain.Goal;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalCreateResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.exception.SubGoalException;
import com.hotpack.krocs.domain.goals.exception.SubGoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.GoalRepositoryFacade;
import com.hotpack.krocs.domain.goals.facade.SubGoalRepositoryFacade;
import com.hotpack.krocs.global.common.entity.Priority;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GoalServiceTest {

  @Mock
  private GoalRepositoryFacade goalRepositoryFacade;
  @Mock
  private SubGoalRepositoryFacade subGoalRepositoryFacade;

  @Mock
  private GoalConvertor goalConvertor;

  @InjectMocks
  private GoalServiceImpl goalService;

  private CreateGoalRequestDTO validRequestDTO;
  private Goal validGoal;
  private CreateGoalResponseDTO validResponseDTO;

  private SubGoalCreateRequestDTO validSubGoalCreateRequestDTO;
  private SubGoalCreateResponseDTO validSubGoalCreateResponseDTO;
  private SubGoal validSubGoal;
  private SubGoalResponseDTO validSubGoalResponseDTO;
  private SubGoalRequestDTO validSubGoalRequestDTO;

  @BeforeEach
  void setUp() {
    validRequestDTO = CreateGoalRequestDTO.builder()
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

    validResponseDTO = CreateGoalResponseDTO.builder()
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

    // 소목표 valid mock data
    validSubGoalRequestDTO = SubGoalRequestDTO.builder()
        .title("테스트 소목표1")
        .build();

    validSubGoalCreateRequestDTO = SubGoalCreateRequestDTO.builder()
        .subGoals(List.of(validSubGoalRequestDTO))
        .build();

    validSubGoal = SubGoal.builder()
        .subGoalId(1L)
        .goal(validGoal)
        .title("테스트 소목표1")
        .isCompleted(false)
        .build();

    validSubGoalResponseDTO = SubGoalResponseDTO.builder()
        .subGoalId(validSubGoal.getSubGoalId())
        .title(validSubGoal.getTitle())
        .completed(validSubGoal.getIsCompleted())
        .completionPercentage(0)
        .build();

    validSubGoalCreateResponseDTO = SubGoalCreateResponseDTO.builder()
        .goalId(1L)
        .createdSubGoals(List.of(validSubGoalResponseDTO))
        .build();
  }

  @Test
  @DisplayName("대목표 생성 성공 테스트")
  void createGoal_Success() {
    // given
    when(goalConvertor.toEntity(validRequestDTO)).thenReturn(validGoal);
    when(goalRepositoryFacade.saveGoal(validGoal)).thenReturn(validGoal);
    when(goalConvertor.toCreateResponseDTO(validGoal)).thenReturn(validResponseDTO);

    // when
    CreateGoalResponseDTO result = goalService.createGoal(validRequestDTO, 1L);

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
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
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
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
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
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
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
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
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
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
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
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
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
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
        .title("테스트 목표")
        .startDate(LocalDate.of(2024, 12, 31))
        .endDate(LocalDate.of(2024, 1, 1))
        .duration(30)
        .build();

    // when & then
    assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
        .isInstanceOf(GoalException.class)
        .hasFieldOrPropertyWithValue("goalExceptionType",
            GoalExceptionType.INVALID_GOAL_DATE_RANGE);
  }

  @Test
  @DisplayName("대목표 생성 - 시작 날짜가 과거인 경우")
  void createGoal_StartDateInPast() {
    // given
    CreateGoalRequestDTO invalidRequest = CreateGoalRequestDTO.builder()
        .title("테스트 목표")
        .startDate(LocalDate.now().minusDays(1))
        .duration(30)
        .build();

    // when & then
    assertThatThrownBy(() -> goalService.createGoal(invalidRequest, 1L))
        .isInstanceOf(GoalException.class)
        .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_DATE_IN_PAST);
  }

  @Test
  @DisplayName("대목표 생성 - Repository에서 예외 발생")
  void createGoal_RepositoryException() {
    // given
    when(goalConvertor.toEntity(any())).thenReturn(validGoal);
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
    when(goalConvertor.toEntity(any())).thenThrow(new RuntimeException("변환 오류"));

    // when & then
    assertThatThrownBy(() -> goalService.createGoal(validRequestDTO, 1L))
        .isInstanceOf(GoalException.class)
        .hasFieldOrPropertyWithValue("goalExceptionType", GoalExceptionType.GOAL_CREATION_FAILED);
  }

  @Test
  @DisplayName("대목표 생성 - 최소 필수 데이터만으로 성공")
  void createGoal_MinimalData() {
    // given
    CreateGoalRequestDTO minimalRequest = CreateGoalRequestDTO.builder()
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

    CreateGoalResponseDTO minimalResponse = CreateGoalResponseDTO.builder()
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
    CreateGoalResponseDTO result = goalService.createGoal(minimalRequest, 1L);

    // then
    assertThat(result).isNotNull();
    assertThat(result.getTitle()).isEqualTo("최소 목표");
    assertThat(result.getPriority()).isEqualTo(Priority.MEDIUM);
    assertThat(result.getDuration()).isEqualTo(1);
  }

  @Test
  @DisplayName("소목표 생성 성공 테스트")
  void createSubGoals_Success() {
    // given
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);
    when(subGoalRepositoryFacade.saveSubGoals(List.of(validSubGoal))).thenReturn(
        List.of(validSubGoal));
    when(goalConvertor.toSubGoalEntity(validGoal, validSubGoalRequestDTO)).thenReturn(validSubGoal);
    when(goalConvertor.toSubGoalResponseDTO(any(SubGoal.class)))
        .thenReturn(validSubGoalResponseDTO);

    // when
    SubGoalCreateResponseDTO result = goalService.createSubGoals(1L, validSubGoalCreateRequestDTO);

    // then
    assertThat(result).isNotNull();
    assertThat(result.goalId()).isEqualTo(1L);
    assertThat(result.createdSubGoals()).isNotEmpty();
    for (SubGoalResponseDTO subGoalRequestDTO : result.createdSubGoals()) {
      assertThat(subGoalRequestDTO.getSubGoalId()).isEqualTo(1L);
      assertThat(subGoalRequestDTO.getTitle()).isEqualTo("테스트 소목표1");
      assertThat(subGoalRequestDTO.getCompleted()).isEqualTo(false);
      assertThat(subGoalRequestDTO.getCompletionPercentage()).isEqualTo(0);
    }
  }

  @Test
  @DisplayName("소목표 생성 - GoalRepository에서 예외 발생")
  void createSubGoal_GoalsRepositoryException() {
    // given
    when(goalRepositoryFacade.findGoalById(any())).thenThrow(new RuntimeException("데이터베이스 오류"));

    // when & then
    assertThatThrownBy(() -> goalService.createSubGoals(1L, validSubGoalCreateRequestDTO))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_CREATE_FAILED);
  }

  @Test
  @DisplayName("소목표 생성 - Goal 조회 실패")
  void createSubGoal_GoalsRepositoryNotFound() {
    // given
    when(goalRepositoryFacade.findGoalById(any())).thenReturn(null);

    // when & then
    assertThatThrownBy(() -> goalService.createSubGoals(1L, validSubGoalCreateRequestDTO))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_GOAL_NOT_FOUND);
  }

  @Test
  @DisplayName("소목표 생성 - SubGoalCreateRequest.subGoals()가 비어있는 리스트인 경우")
  void createSubGoals_subGoalsIsEmpty() {
    // given
    SubGoalCreateRequestDTO invalidSubGoalCreateRequestDTO = SubGoalCreateRequestDTO
        .builder()
        .subGoals(new ArrayList<>())
        .build();

    // when & then
    assertThatThrownBy(() -> goalService.createSubGoals(1L, invalidSubGoalCreateRequestDTO))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_CREATE_EMPTY);
  }

  @Test
  @DisplayName("소목표 생성 - SubGoalRequestDTO.title()이 Blank인 경우")
  void createSubGoals_titleIsBlank() {
    // given
    SubGoalRequestDTO invalidSubGoalRequestDTO = SubGoalRequestDTO
        .builder()
        .title("")
        .build();
    SubGoalCreateRequestDTO invalidSubGoalCreateRequestDTO = SubGoalCreateRequestDTO
        .builder()
        .subGoals(List.of(invalidSubGoalRequestDTO))
        .build();

    // when & then
    assertThatThrownBy(() -> goalService.createSubGoals(1L, invalidSubGoalCreateRequestDTO))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_TITLE_EMPTY);
  }

  @Test
  @DisplayName("소목표 생성 - SubGoalRequestDTO.title()이 200자를 초과하는 경우")
  void createSubGoals_titleExceedsMaxLength() {
    // given
    SubGoalRequestDTO invalidSubGoalRequestDTO = SubGoalRequestDTO
        .builder()
        .title(
            "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901")
        .build();
    SubGoalCreateRequestDTO invalidSubGoalCreateRequestDTO = SubGoalCreateRequestDTO
        .builder()
        .subGoals(List.of(invalidSubGoalRequestDTO))
        .build();

    // when & then
    assertThatThrownBy(() -> goalService.createSubGoals(1L, invalidSubGoalCreateRequestDTO))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_TITLE_TOO_LONG);
  }

  @Test
  @DisplayName("소목표 생성 - SubGoalRepository 저장 실패")
  void createSubGoal_SubGoalsRepositoryException() {
    // given
    when(subGoalRepositoryFacade.saveSubGoals(any())).thenThrow(new RuntimeException("데이터베이스 오류"));
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);

    // when & then
    assertThatThrownBy(() -> goalService.createSubGoals(1L, validSubGoalCreateRequestDTO))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_CREATE_FAILED);
  }

} 