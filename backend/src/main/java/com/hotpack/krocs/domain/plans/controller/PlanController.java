package com.hotpack.krocs.domain.plans.controller;

import com.hotpack.krocs.domain.plans.dto.request.PlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.PlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.exception.PlanException;
import com.hotpack.krocs.domain.plans.exception.PlanExceptionType;
import com.hotpack.krocs.domain.plans.service.PlanService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    public ApiResponse<PlanCreateResponseDTO> createPlan(
            @Valid @RequestBody PlanCreateRequestDTO requestDTO,
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam(value = "sub_goal_id", required = false) Long subGoalId
    ) {
        try {
            PlanCreateResponseDTO responseDTO = planService.createPlan(requestDTO, userId, subGoalId);
            return ApiResponse.success(responseDTO);
        } catch (PlanException e) {
            throw e;
        } catch (Exception e) {
            throw new PlanException(PlanExceptionType.PLAN_CREATION_FAILED);
        }
    }
}
