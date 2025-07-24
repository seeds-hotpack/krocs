package com.hotpack.krocs.domain.goals.service;

import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface GoalService {
    /**
     * 대목표를 생성합니다.
     * 
     * @param requestDTO 대목표 생성 요청 데이터
     * @param userId 사용자 ID
     * @return 생성된 대목표 정보
     */
    CreateGoalResponseDTO createGoal(CreateGoalRequestDTO requestDTO, Long userId);

    List<GoalResponseDTO> getGoalByUser(Long userId, LocalDateTime dateTime);
}