package com.hotpack.krocs.domain.goals.controller;

import com.hotpack.krocs.common.response.ApiResponse;
import com.hotpack.krocs.domain.goals.exception.GoalExceptionType;
import com.hotpack.krocs.common.response.exception.handler.GoalHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * GoalHandler 사용 예시 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/goals")
public class GoalController {

    /**
     * 예시 1: 목표를 찾을 수 없는 경우
     */
    @GetMapping("/{goalId}")
    public ApiResponse<String> getGoal(@PathVariable Long goalId) {
        
        // 목표가 존재하지 않는 경우
        if (goalId == null || goalId <= 0) {
            throw new GoalHandler(GoalExceptionType.GOAL_NOT_FOUND);
        }
        
        // 성공 응답
        return ApiResponse.success("목표 조회 성공: " + goalId);
    }

    /**
     * 예시 2: 목표 완료 처리
     */
    @PatchMapping("/{goalId}/complete")
    public ApiResponse<String> completeGoal(@PathVariable Long goalId) {
        
        // 이미 완료된 목표인 경우 (goalId가 짝수인 경우)
        if (goalId % 2 == 0) {
            throw new GoalHandler(GoalExceptionType.GOAL_ALREADY_COMPLETED);
        }
        
        // 성공 응답
        return ApiResponse.success("목표 완료 처리 성공: " + goalId);
    }

    /**
     * 예시 3: 목표 생성 (입력값 검증)
     */
    @PostMapping
    public ApiResponse<String> createGoal(@RequestBody CreateGoalRequest request) {
        
        // 제목이 비어있는 경우
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new GoalHandler(GoalExceptionType.GOAL_TITLE_EMPTY);
        }
        
        // 기간이 유효하지 않은 경우
        if (request.getDuration() != null && request.getDuration() < 1) {
            throw new GoalHandler(GoalExceptionType.GOAL_DURATION_INVALID);
        }
        
        // 성공 응답
        return ApiResponse.success("목표 생성 성공: " + request.getTitle());
    }

    /**
     * 예시 4: 조건부 예외 발생
     */
    @GetMapping("/test/{condition}")
    public ApiResponse<String> testCondition(@PathVariable String condition) {
        
        switch (condition) {
            case "not-found":
                throw new GoalHandler(GoalExceptionType.GOAL_NOT_FOUND);
            case "completed":
                throw new GoalHandler(GoalExceptionType.GOAL_ALREADY_COMPLETED);
            case "invalid":
                throw new GoalHandler(GoalExceptionType.INVALID_GOAL_DATE_RANGE);
            default:
                return ApiResponse.success("정상 처리: " + condition);
        }
    }

    /**
     * 간단한 요청 DTO
     */
    public static class CreateGoalRequest {
        private String title;
        private Integer duration;

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public Integer getDuration() { return duration; }
        public void setDuration(Integer duration) { this.duration = duration; }
    }
}
