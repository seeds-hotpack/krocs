package com.hotpack.krocs.domain.plans.controller;


import com.hotpack.krocs.domain.plans.dto.request.SubPlanCreateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.response.SubPlanCreateResponseDTO;
import com.hotpack.krocs.domain.plans.exception.SubPlanException;
import com.hotpack.krocs.domain.plans.exception.SubPlanExceptionType;
import com.hotpack.krocs.domain.plans.service.SubPlanService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SubPlanController {

    private final SubPlanService subPlanService;


    @Operation(summary = "소계획 생성", description = "소계획을 생성합니다.")
    @PostMapping("plans/{planId}/subplans")
    public ApiResponse<SubPlanCreateResponseDTO> createSubPlans(
        @PathVariable @Parameter(description = "Plan ID", example = "1")
        Long planId,
        @RequestBody @Parameter(description = "SubPlans", example = "{\"title\": \"소계획1\"}")
        SubPlanCreateRequestDTO subPlanCreateRequestDTO) {
        try {
            SubPlanCreateResponseDTO responseDTO = subPlanService.createSubPlans(planId,
                subPlanCreateRequestDTO);
            return ApiResponse.success(responseDTO);
        } catch (SubPlanException e) {
            throw e;
        } catch (Exception e) {
            throw new SubPlanException(SubPlanExceptionType.SUB_PLAN_CREATE_FAILED);
        }
    }
}