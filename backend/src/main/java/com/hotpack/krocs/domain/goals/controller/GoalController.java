package com.hotpack.krocs.domain.goals.controller;

import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.service.GoalService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 대목표 관련 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/goals")
public class GoalController {

    private final GoalService goalService;

    /**
     * 대목표(goal) 생성 API
     * 
     * @param requestDTO 대목표 생성 요청 데이터
     * @param userId 사용자 ID (query param, 선택, 토큰 전 테스트용)
     * @return ApiResponse<CreateGoalResponseDTO>
     */
    @PostMapping
    public ApiResponse<CreateGoalResponseDTO> createGoal(
            @Valid @RequestBody CreateGoalRequestDTO requestDTO,
            @RequestParam(value = "user_id", required = false) Long userId
    ) {
        try {
            CreateGoalResponseDTO responseDTO = goalService.createGoal(requestDTO, userId);
            return ApiResponse.success(responseDTO);

        } catch (GoalException e) {
            throw e;

        } catch (Exception e) {
            throw new GoalException(GoalExceptionType.GOAL_CREATION_FAILED);

        }
    }
}
