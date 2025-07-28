package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.dto.request.GoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalCreateResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalListResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalResponseDTO;

public interface GoalService {
  
  CreateGoalResponseDTO createGoal(GoalCreateRequestDTO requestDTO, Long userId);

  SubGoalCreateResponseDTO createSubGoals(Long goalId, SubGoalCreateRequestDTO requestDTO);

  SubGoalListResponseDTO getAllSubGoals(Long goalId);

  SubGoalResponseDTO getSubGoal(Long goalId, Long subGoalId);
} 