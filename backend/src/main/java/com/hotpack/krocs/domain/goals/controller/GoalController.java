package com.hotpack.krocs.domain.goals.controller;

import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalCreateRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.SubGoalCreateResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.exception.SubGoalException;
import com.hotpack.krocs.domain.goals.exception.SubGoalExceptionType;
import com.hotpack.krocs.domain.goals.service.GoalService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 대목표 관련 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/goals")
@Tag(name = "Goal", description = "Goal 관련 API")
public class GoalController {

  private final GoalService goalService;

  /**
   * 대목표(goal) 생성 API
   *
   * @param requestDTO 대목표 생성 요청 데이터
   * @param userId     사용자 ID (query param, 선택, 토큰 전 테스트용)
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


  /**
   * 소목표(sub_goal) 생성 API
   *
   * @param goalId                  대목표(Goal) ID
   * @param subGoalCreateRequestDTO 소목표(SubGoal) 리스트
   * @return ApiResponse<SubGoalCreateResponseDTO>
   */
  @Operation(summary = "소목표 생성", description = "소목표를 생성합니다.")
  @PostMapping("/{goalId}/subgoals")
  public ApiResponse<SubGoalCreateResponseDTO> createSubGoals(
      @PathVariable @Parameter(description = "Goal ID", example = "1")
      Long goalId,
      @RequestBody @Parameter(description = "SubGoals", example = "{\"title\": \"소목표1\", \"energy\": 50}")
      SubGoalCreateRequestDTO subGoalCreateRequestDTO) {
    try {
      SubGoalCreateResponseDTO responseDTO = goalService.createSubGoals(goalId,
          subGoalCreateRequestDTO);

      return ApiResponse.success(responseDTO);
    } catch (SubGoalException e) {
      throw e;
    } catch (Exception e) {
      throw new SubGoalException(SubGoalExceptionType.SUB_GOAL_CREATE_FAILED);
    }


  }

}
