package com.hotpack.krocs.domain.plans.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class PlanResponseDTO {

    private Long planId;
    private Long goalId;
    private Long subGoalId;

    private String title;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    private Boolean allDay;

    private Boolean isCompleted;

    private LocalDateTime completedAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
