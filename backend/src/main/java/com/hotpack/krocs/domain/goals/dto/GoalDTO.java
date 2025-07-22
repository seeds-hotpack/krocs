package com.hotpack.krocs.domain.goals.dto;

import com.hotpack.krocs.common.entity.Priority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

public class GoalDTO {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateGoalRequest {
        private String title;
        private Priority priority;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer duration;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateGoalRequest {
        private String title;
        private Priority priority;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer duration;
        private Boolean isCompleted;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GoalResponse {
        private Long goalId;
        private String title;
        private Priority priority;
        private LocalDate startDate;
        private LocalDate endDate;
        private Integer duration;
        private Boolean isCompleted;
        private LocalDate createdAt;
        private LocalDate updatedAt;
        // private List<SubGoalDTO.SubGoalResponse> subGoals; // SubGoalDTO 구현 후 활성화
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GoalListResponse {
        private List<GoalResponse> goals;
        private int totalCount;
        private int pageNumber;
        private int pageSize;
    }
}
