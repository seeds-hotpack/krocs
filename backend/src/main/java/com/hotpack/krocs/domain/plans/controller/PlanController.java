package com.hotpack.krocs.domain.plans.controller;

import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.request.PlanUpdateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanListResponseDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanResponseDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import com.hotpack.krocs.domain.plans.service.PlanService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/plans")
@Tag(name = "Plan", description = "Plan 관련 API")
public class PlanController {

    private final PlanService planService;

    @Operation(summary = "일정 생성", description = "새로운 일정을 생성합니다.")
    @PostMapping
    public ApiResponse<PlanResponseDTO> createPlan(
            @Valid @RequestBody PlanCreateRequestDTO requestDTO,
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam(value = "sub_goal_id", required = false) Long subGoalId
    ) {
        try {
            PlanResponseDTO responseDTO = planService.createPlan(requestDTO, userId, subGoalId);
            return ApiResponse.success(responseDTO);
        } catch (PlanException e) {
            throw e;
        } catch (Exception e) {
            throw new PlanException(PlanExceptionType.PLAN_CREATION_FAILED);
        }
    }

    @Operation(summary = "모든 일정 조회", description = "모든 일정을 조회합니다.")
    @GetMapping
    public ApiResponse<PlanListResponseDTO> getPlans(
        @RequestParam(value = "user_id", required = false) Long userId
    ) {
        try {
            PlanListResponseDTO response = planService.getAllPlans(userId);
            return ApiResponse.success(response);
        } catch (PlanException e) {
            throw e;
        } catch (Exception e) {
            throw new PlanException(PlanExceptionType.PLAN_FOUND_FAILED);
        }
    }

    @Operation(summary = "특정 일정 조회", description = "특정 일정을 조회합니다.")
    @GetMapping("/{planId}")
    public ApiResponse<PlanResponseDTO> getPlanById(
        @PathVariable @Parameter(description = "Plan ID", example = "1")
        Long planId,
        @RequestParam(value = "user_id", required = false) Long userId
    ) {
        try {
            PlanResponseDTO response = planService.getPlanById(planId, userId);
            return ApiResponse.success(response);
        } catch (PlanException e) {
            throw e;
        } catch (Exception e) {
            throw new PlanException(PlanExceptionType.PLAN_FOUND_FAILED);
        }
    }

    @Operation(summary = "일정 수정", description = "기존 일정의 정보를 수정합니다."
    )
    @PatchMapping("/{planId}")
    public ApiResponse<PlanResponseDTO> updatePlanById(
        @PathVariable Long planId,
        @Valid @RequestBody PlanUpdateRequestDTO request,
        @RequestParam(value = "user_id", required = false) Long userId,
        @RequestParam(value = "sub_goal_id", required = false) Long subGoalId) {
        try{
            PlanResponseDTO responseDTO = planService.updatePlanById(planId, subGoalId, request, userId);

            return ApiResponse.success(responseDTO);
        } catch (PlanException e) {
            throw e;
        } catch (Exception e) {
            throw new PlanException(PlanExceptionType.PLAN_UPDATE_FAILED);
        }
    }

    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다")
    @DeleteMapping("/{planId}")
    public ApiResponse<Void> deleteSubPlan(
        @PathVariable @Parameter(description = "Plan ID", example = "1") Long planId,
        @RequestParam(value = "user_id", required = false) Long userId
    ) {
        try {
            planService.deletePlan(planId, userId);
            return ApiResponse.success();
        } catch (PlanException e) {
            throw e;
        } catch (Exception e) {
            throw new PlanException(PlanExceptionType.PLAN_DELETE_FAILED);
        }
    }
}
