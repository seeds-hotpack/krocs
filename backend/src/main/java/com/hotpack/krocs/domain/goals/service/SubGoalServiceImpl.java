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

      // TODO: 테스트 하세욥
      return SubGoalConvertor.toSubGoalUpdateResponseDTO(subGoal);
    } catch (SubGoalException e) {
      throw e;
    } catch (Exception e) {
      log.error("대목표 수정 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_UPDATE_FAILED);
    }
  }

  private void validateBusinessRules(SubGoalUpdateRequestDTO requestDTO) {
    if (requestDTO.title().length() > 200) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_TITLE_TOO_LONG);
    }
  }

}
