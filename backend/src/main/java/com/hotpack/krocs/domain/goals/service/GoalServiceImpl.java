package com.hotpack.krocs.domain.goals.service;

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
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalServiceImpl implements GoalService {

  private final GoalRepositoryFacade goalRepositoryFacade;
  private final SubGoalRepositoryFacade subGoalRepositoryFacade;
  private final GoalConvertor goalConvertor;

  @Override
  @Transactional
  public CreateGoalResponseDTO createGoal(CreateGoalRequestDTO requestDTO, Long userId) {
    try {
      validateGoalCreation(requestDTO);
      validateBusinessRules(requestDTO, userId);

      Goal goal = goalConvertor.toEntity(requestDTO);
      Goal savedGoal = goalRepositoryFacade.saveGoal(goal);

      return goalConvertor.toCreateResponseDTO(savedGoal);
    } catch (GoalException e) {
      throw e;
    } catch (Exception e) {
      log.error("대목표 생성 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
      throw new GoalException(GoalExceptionType.GOAL_CREATION_FAILED);
    }
  }

  /**
   * 대목표 생성 시 기본 검증을 수행합니다.
   *
   * @param requestDTO 생성 요청 DTO
   */
  private void validateGoalCreation(CreateGoalRequestDTO requestDTO) {
    // 제목 검증
    if (!StringUtils.hasText(requestDTO.getTitle())) {
      throw new GoalException(GoalExceptionType.GOAL_TITLE_EMPTY);
    }

    if (requestDTO.getTitle().length() > 200) {
      throw new GoalException(GoalExceptionType.GOAL_TITLE_TOO_LONG);
    }

    // 기간 검증
    if (requestDTO.getDuration() == null || requestDTO.getDuration() <= 0) {
      throw new GoalException(GoalExceptionType.GOAL_DURATION_INVALID);
    }

    // 날짜 범위 검증
    if (requestDTO.getStartDate() != null && requestDTO.getEndDate() != null) {
      if (requestDTO.getStartDate().isAfter(requestDTO.getEndDate())) {
        throw new GoalException(GoalExceptionType.INVALID_GOAL_DATE_RANGE);
      }
    }
  }

  /**
   * 대목표 생성 시 비즈니스 규칙 검증을 수행합니다.
   *
   * @param requestDTO 생성 요청 DTO
   * @param userId     사용자 ID
   */
  private void validateBusinessRules(CreateGoalRequestDTO requestDTO, Long userId) {
    // 시작 날짜가 과거인지 검증
    if (requestDTO.getStartDate() != null) {
      java.time.LocalDate today = java.time.LocalDate.now();
      if (requestDTO.getStartDate().isBefore(today)) {
        throw new GoalException(GoalExceptionType.GOAL_DATE_IN_PAST);
      }
    }
  }

  @Override
  @Transactional
  public SubGoalCreateResponseDTO createSubGoals(Long goalId, SubGoalCreateRequestDTO requestDTO) {
    try {
      if (goalId == null) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_GOAL_ID_IS_NULL);
      }
      validateSubGoalCreation(requestDTO);

      Goal goal = goalRepositoryFacade.findGoalById(goalId);

      List<SubGoal> subGoals = new ArrayList<>();
      for (SubGoalRequestDTO subGoalRequestDTO : requestDTO.subGoals()) {
        subGoals.add(goalConvertor.toSubGoalEntity(goal, subGoalRequestDTO));
      }

      List<SubGoal> createdSubGoals = subGoalRepositoryFacade.saveSubGoals(subGoals);
      List<SubGoalResponseDTO> subGoalResponseDTOs = convertToListSubGoalResponseDTO(
          createdSubGoals);

      return new SubGoalCreateResponseDTO(goalId, subGoalResponseDTOs);
    } catch (SubGoalException e) {
      throw e;
    } catch (Exception e) {
      log.error("소목표 생성 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_CREATE_FAILED);
    }
  }

  /**
   * 소목표 생성시 기본 검증을 수행합니다.
   *
   * @param subGoalCreateRequestDTO
   */
  private void validateSubGoalCreation(SubGoalCreateRequestDTO subGoalCreateRequestDTO) {
    if (subGoalCreateRequestDTO.subGoals().isEmpty()) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_CREATE_EMPTY);
    }

    for (SubGoalRequestDTO subGoalRequestDTO : subGoalCreateRequestDTO.subGoals()) {
      if (subGoalRequestDTO.title().isBlank()) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_TITLE_EMPTY);
      }
      if (subGoalRequestDTO.title().length() > 200) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_TITLE_TOO_LONG);
      }
    }
  }

  /**
   * List<SubGoal>을 List<SubGoalResponseDTO>로 변환합니다.
   *
   * @param subGoals 변환할 SubGoal
   * @return List<SubGoalResponseDTO>
   */
  private List<SubGoalResponseDTO> convertToListSubGoalResponseDTO(List<SubGoal> subGoals) {
    List<SubGoalResponseDTO> subGoalResponseDTOs = new ArrayList<>();
    for (SubGoal subGoal : subGoals) {
      subGoalResponseDTOs.add(goalConvertor.toSubGoalResponseDTO(subGoal));
    }
    return subGoalResponseDTOs;
  }

  /**
   * 모든 subGoal을 반환합니다.
   *
   * @param goalId 소목표(SubGoal)를 조회할 대목표(Goal) ID
   * @return SubGoalListResponseDTO
   */
  @Override
  public SubGoalListResponseDTO getAllSubGoals(Long goalId) {
    try {
      if (goalId == null) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_GOAL_ID_IS_NULL);
      }

      Goal goal = goalRepositoryFacade.findGoalById(goalId);

      List<SubGoal> subGoals = subGoalRepositoryFacade.findSubGoalsByGoal(goal);
      if (subGoals.isEmpty()) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_FOUND);
      }
      List<SubGoalResponseDTO> subGoalResponseDTOS = convertToListSubGoalResponseDTO(subGoals);

      return new SubGoalListResponseDTO(subGoalResponseDTOS);
    } catch (SubGoalException e) {
      throw e;
    } catch (Exception e) {
      log.error("소목표 전체 조회 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_READ_FAILED);
    }
  }

  @Override
  public SubGoalResponseDTO getSubGoal(Long goalId, Long subGoalId) {
    try {

      if (goalId == null) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_GOAL_ID_IS_NULL);
      }
      if (subGoalId == null) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_ID_IS_NULL);
      }

      Goal goal = goalRepositoryFacade.findGoalById(goalId);
      List<SubGoal> subGoals = subGoalRepositoryFacade.findSubGoalsByGoal(goal);
      SubGoal subGoal = subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId);
      if (!subGoals.contains(subGoal)) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_NOT_BELONG_TO_GOAL);
      }
      return goalConvertor.toSubGoalResponseDTO(subGoal);

    } catch (SubGoalException e) {
      throw e;
    } catch (Exception e) {
      log.error("소목표 단건 조회 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_READ_FAILED);
    }
  }
}