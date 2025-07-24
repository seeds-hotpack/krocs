package com.hotpack.krocs.domain.goals.controller;

import com.hotpack.krocs.domain.goals.dto.request.CreateGoalRequestDTO;
import com.hotpack.krocs.domain.goals.dto.response.CreateGoalResponseDTO;
import com.hotpack.krocs.domain.goals.dto.response.GoalResponseDTO;
import com.hotpack.krocs.domain.goals.exception.GoalException;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.domain.goals.service.GoalService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

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
    @Operation(
            summary = "대목표 생성",
            description = "새로운 대목표를 생성합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "목표 생성 성공",
                    content = @Content(schema = @Schema(implementation = CreateGoalResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content
            )
    })
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

    @Operation(
            summary = "내 대목표 목록 조회",
            description = "사용자의 대목표 목록을 조회합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "대목표 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = GoalResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "목표 조회 실패",
                    content = @Content
            )
    })
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

    @Operation(
            summary = "특정 목표 상세 조회",
            description = "특정 목표의 상세 정보를 조회합니다."
//            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "목표 상세 조회 성공",
                    content = @Content(schema = @Schema(implementation = GoalResponseDTO.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 데이터",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "접근 권한 없음",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "일정을 찾을 수 없음",
                    content = @Content
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "목표 조회 실패",
                    content = @Content
            )
    })
    @GetMapping("/{goalId}")
    public ApiResponse<GoalResponseDTO> getCalendar(
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
}
