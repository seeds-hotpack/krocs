package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.convertor.SubGoalConvertor;
import com.hotpack.krocs.domain.goals.domain.SubGoal;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalUpdateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalUpdateResponseDTO;
import com.hotpack.krocs.domain.goals.exception.SubGoalException;
import com.hotpack.krocs.domain.goals.exception.SubGoalExceptionType;
import com.hotpack.krocs.domain.goals.facade.SubGoalRepositoryFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubGoalServiceImpl implements SubGoalService {

  private final SubGoalRepositoryFacade subGoalRepositoryFacade;

  /**
   * 소목표 수정 메서드
   *
   * <p>주어진 소목표 ID에 해당하는 SubGoal 엔티티를 찾아서,
   * 요청으로 전달된 제목 또는 완료 여부를 기반으로 값을 수정한다. 수정 전에는 제목 길이(최대 200자)에 대한 비즈니스 유효성 검사를 수행한다.
   *
   * <p>예외 상황:
   * - subGoalId가 null인 경우: SUB_GOAL_ID_IS_NULL 예외 발생 - 제목 길이가 200자를 초과한 경우: SUB_GOAL_TITLE_TOO_LONG
   * 예외 발생 - 소목표 조회 실패 등 예외 발생 시: SUB_GOAL_UPDATE_FAILED 예외 발생
   *
   * @param subGoalId  수정할 소목표의 ID
   * @param requestDTO 수정 요청 정보 (제목, 완료 여부)
   * @return 수정된 소목표의 응답 DTO
   * @throws SubGoalException 비즈니스 예외 또는 처리 실패 시 발생
   */
  @Override
  @Transactional
  public SubGoalUpdateResponseDTO updateSubGoal(Long subGoalId,
      SubGoalUpdateRequestDTO requestDTO) {
    try {
      validateBusinessRules(requestDTO);
      if (subGoalId == null) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_ID_IS_NULL);
      }

      SubGoal subGoal = subGoalRepositoryFacade.findSubGoalBySubGoalId(subGoalId);
      if (requestDTO.title() != null) {
        subGoal.setTitle(requestDTO.title());
      }
      if (requestDTO.isCompleted() != null) {
        subGoal.setIsCompleted(requestDTO.isCompleted());
      }

      return SubGoalConvertor.toSubGoalUpdateResponseDTO(subGoal);
    } catch (SubGoalException e) {
      throw e;
    } catch (Exception e) {
      log.error("소목표 수정 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_UPDATE_FAILED);
    }
  }

  /**
   * 소목표 제목 유효성 검사
   *
   * <p>소목표 제목이 200자를 초과하는 경우 예외를 발생시킨다.
   *
   * @param requestDTO 수정 요청 DTO
   * @throws SubGoalException 제목이 200자를 초과하면 SUB_GOAL_TITLE_TOO_LONG 발생
   */
  private void validateBusinessRules(SubGoalUpdateRequestDTO requestDTO) {
    if (requestDTO.title().length() > 200) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_TITLE_TOO_LONG);
    }
  }

  /**
   * 소목표 삭제 메서드
   *
   * <p>주어진 소목표 ID에 해당하는 SubGoal 엔티티를 삭제한다.
   * 내부적으로 존재 여부는 확인하지 않으며, 삭제할 항목이 없을 경우 JPA는 예외를 던지지 않는다.
   *
   * <p>예외 상황:
   * - subGoalId가 null인 경우: SUB_GOAL_ID_IS_NULL 예외 발생 - 기타 처리 중 오류 발생 시: SUB_GOAL_DELETE_FAILED 예외
   * 발생
   *
   * @param subGoalId 삭제할 소목표의 ID
   * @throws SubGoalException 비즈니스 예외 또는 처리 실패 시 발생
   */
  @Override
  @Transactional
  public void deleteSubGoal(Long subGoalId) {
    try {
      if (subGoalId == null) {
        throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_ID_IS_NULL);
      }
      subGoalRepositoryFacade.deleteSubGoalBySubGoalId(subGoalId);
    } catch (SubGoalException e) {
      throw e;
    } catch (Exception e) {
      log.error("소목표 삭제 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_DELETE_FAILED);
    }
  }

}
