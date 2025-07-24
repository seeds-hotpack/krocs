package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.UpdateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public interface GoalService {

    CreateGoalResponseDTO createGoal(CreateGoalRequestDTO requestDTO, Long userId);

    List<GoalResponseDTO> getGoalByUser(Long userId, LocalDateTime dateTime);

    GoalResponseDTO getGoalByGoalId(Long userId, Long goalId);

    GoalResponseDTO updateGoalById(Long goalId, UpdateGoalRequestDTO request, Long userId);

    void deleteGoal(Long userId, Long goalId);
}