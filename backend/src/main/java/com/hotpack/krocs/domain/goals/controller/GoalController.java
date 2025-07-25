package com.hotpack.krocs.domain.goals.controller;

import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.request.UpdateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.service.GoalService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/goals")
public class GoalController {

    private final GoalService goalService;

    @Operation(summary = "대목표 생성", description = "새로운 대목표를 생성합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
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

    @Operation(summary = "대목표 목록 조회", description = "사용자의 대목표 목록을 조회합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ApiResponse<List<GoalResponseDTO>> getGoal(
            @RequestParam(value = "user_id", required = false) Long userId, @RequestParam(required = false) LocalDateTime dateTime
    ){
        try{
            if(dateTime == null) {
                dateTime = LocalDateTime.now();
            }
            List<GoalResponseDTO> responseDTO = goalService.getGoalByUser(userId, dateTime);
            return ApiResponse.success(responseDTO);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            throw new GoalException(GoalExceptionType.GOAL_FOUND_FAILED);
        }
    }

    @Operation(summary = "특정 목표 상세 조회", description = "특정 목표의 상세 정보를 조회합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{goalId}")
    public ApiResponse<GoalResponseDTO> getGoalById(
            @PathVariable Long goalId,
            @RequestParam(value = "user_id", required = false) Long userId) {
        try{
            GoalResponseDTO responseDTO = goalService.getGoalByGoalId(userId, goalId);
            return ApiResponse.success(responseDTO);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            throw new GoalException(GoalExceptionType.GOAL_FOUND_FAILED);
        }
    }

    @Operation(summary = "목표 수정", description = "기존 목표의 정보를 수정합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{goalId}")
    public ApiResponse<GoalResponseDTO> updateGoalById(
            @PathVariable Long goalId,
            @Valid @RequestBody UpdateGoalRequestDTO request,
            @RequestParam(value = "user_id", required = false) Long userId) {
        try{
            GoalResponseDTO responseDTO = goalService.updateGoalById(goalId, request, userId);

            return ApiResponse.success(responseDTO);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            throw new GoalException(GoalExceptionType.GOAL_UPDATE_FAILED);
        }
    }

    @Operation(summary = "목표 삭제", description = "기존 목표를 삭제합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
    @DeleteMapping("/{goalId}")
    public ApiResponse<Void> deleteGoal(
            @PathVariable Long goalId,
            @RequestParam(value = "user_id", required = false) Long userId) {
        try{
            goalService.deleteGoal(userId, goalId);
            return ApiResponse.success(null);
        } catch (GoalException e) {
            throw e;
        } catch (Exception e) {
            throw new GoalException(GoalExceptionType.GOAL_DELETE_FAILED);
        }
    }
}
