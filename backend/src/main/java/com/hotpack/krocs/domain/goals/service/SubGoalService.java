package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.dto.request.SubGoalUpdateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalUpdateResponseDTO;

public interface SubGoalService {

  SubGoalUpdateResponseDTO updateSubGoal(Long subGoalId, SubGoalUpdateRequestDTO requestDTO);
}
