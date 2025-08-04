package com.hotpack.krocs.domain.plans.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlanResponseDTO {

    @JsonProperty("plan_id")
    private Long planId;
    @JsonProperty("goal_id")
    private Long goalId;
    @JsonProperty("sub_goal_id")
    private Long subGoalId;
    private String title;

    @Schema(
        description = "시작 일시",
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        example = "2025-08-03T15:05:12"
    )
    @JsonProperty("start_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @Schema(
        description = "종료 일시",
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        example = "2025-08-03T15:05:12"
    )
    @JsonProperty("end_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    @JsonProperty("all_day")
    private Boolean allDay;

    @JsonProperty("is_completed")
    private Boolean isCompleted;

    @Schema(
        description = "완료 일시",
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        example = "2025-08-03T15:05:12"
    )
    @JsonProperty("completed_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime completedAt;

    @Schema(
        description = "생성 일시",
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        example = "2025-08-03T15:05:12"
    )
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(
        description = "수정 일시",
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        example = "2025-08-03T15:05:12"
    )
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
