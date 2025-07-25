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
import com.hotpack.krocs.domain.goals.dto.response.SubGoalListResponseDTO;
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
    when(goalRepositoryFacade.findGoalById(any()))
        .thenThrow(new SubGoalException(SubGoalExceptionType.SUB_GOAL_GOAL_NOT_FOUND));

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

  // SubGoal 전체 조회 test code
  @Test
  @DisplayName("소목표 전체 조회 성공")
  void getAllSubGoals_Success() {
    // given
    List<SubGoal> subGoals = new ArrayList<>();
    subGoals.add(validSubGoal);
    subGoals.add(validSubGoal);
    subGoals.add(validSubGoal);
    subGoals.add(validSubGoal);

    when(subGoalRepositoryFacade.findSubGoalsByGoal(validGoal)).thenReturn(subGoals);
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);
    when(goalConvertor.toSubGoalResponseDTO(any())).thenReturn(validSubGoalResponseDTO);

    // when
    SubGoalListResponseDTO subGoalListResponseDTO = goalService.getAllSubGoals(1L);

    // then
    assertThat(subGoalListResponseDTO.subGoals().size()).isEqualTo(4);
    System.out.println(subGoalListResponseDTO.subGoals().toString());
    assertThat(subGoalListResponseDTO.subGoals().get(0).getSubGoalId()).isEqualTo(1L);
    assertThat(subGoalListResponseDTO.subGoals().getFirst().getCompleted()).isEqualTo(false);
    assertThat(subGoalListResponseDTO.subGoals().getFirst().getTitle()).isEqualTo("테스트 소목표1");
    assertThat(subGoalListResponseDTO.subGoals().getFirst().getCompletionPercentage()).isEqualTo(0);
  }

  @Test
  @DisplayName("소목표 전체 조회 - goalId가 null인 경우")
  void getAllSubGoals_goalIdIsNull() {
    // when & then
    assertThatThrownBy(() -> goalService.getAllSubGoals(null))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_GOAL_ID_IS_NULL);

  }

  @Test
  @DisplayName("소목표 전체 조회 - SubGoalRepository에서 조회 중 예상치 못한 오류가 발생하는 경우")
  void getAllSubGoals_SubGoalRepositoryException() {
    // given
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);
    when(subGoalRepositoryFacade.findSubGoalsByGoal(any())).thenThrow(new RuntimeException());

    // when & then
    assertThatThrownBy(() -> goalService.getAllSubGoals(1L))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_READ_FAILED);
  }

  @Test
  @DisplayName("소목표 전체 조회 - 조회된 SubGoal이 한 건도 없는 경우")
  void getAllSubGoals_SubGoalIsNull() {
    // given
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);
    when(subGoalRepositoryFacade.findSubGoalsByGoal(any())).thenReturn(new ArrayList<>());

    // when & then
    assertThatThrownBy(() -> goalService.getAllSubGoals(1L))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
  }

  @Test
  @DisplayName("소목표 단건 조회 성공")
  void getSubGoal_Success() {
    // given
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);
    when(subGoalRepositoryFacade.findSubGoalsByGoal(validGoal)).thenReturn(List.of(validSubGoal));
    when(subGoalRepositoryFacade.findSubGoalBySubGoalId(1L)).thenReturn(validSubGoal);

    // when
    SubGoalResponseDTO response = goalService.getSubGoal(1L, 1L);

    // then
    assertThat(response).isEqualTo(goalConvertor.toSubGoalResponseDTO(validSubGoal));
  }

  @Test
  @DisplayName("소목표 단건 조회 - subGoalId가 null인 경우")
  void getSubGoal_subGoalIdIsNull() {
    // when & then
    assertThatThrownBy(() -> goalService.getSubGoal(1L, null))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_ID_IS_NULL);

  }

  @Test
  @DisplayName("소목표 단건 조회 - SubGoalRepository 조회 결과 없는 경우")
  void getSubGoal_subGoalRepositoryResultIsNull() {
    // given
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);
    when(subGoalRepositoryFacade.findSubGoalsByGoal(validGoal)).thenReturn(List.of(validSubGoal));
    when(subGoalRepositoryFacade.findSubGoalBySubGoalId(1L)).thenThrow(
        new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_FOUND));

    // when & then
    assertThatThrownBy(() -> goalService.getSubGoal(1L, 1L))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
  }

  @Test
  @DisplayName("소목표 단건 조회 - 소목표가 해당 목표에 속하지 않음")
  void getSubGoal_SubGoalNotBelongToGoal() {
    // given
    when(goalRepositoryFacade.findGoalById(1L)).thenReturn(validGoal);
    when(subGoalRepositoryFacade.findSubGoalsByGoal(validGoal)).thenReturn(new ArrayList<>());
    when(subGoalRepositoryFacade.findSubGoalBySubGoalId(1L)).thenReturn(validSubGoal);

    // when & then
    assertThatThrownBy(() -> goalService.getSubGoal(1L, 1L))
        .isInstanceOf(SubGoalException.class)
        .hasFieldOrPropertyWithValue("subGoalExceptionType",
            SubGoalExceptionType.SUB_GOAL_NOT_BELONG_TO_GOAL);
  }

} 