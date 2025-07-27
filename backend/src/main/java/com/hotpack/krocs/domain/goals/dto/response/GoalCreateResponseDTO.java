package com.hotpack.krocs.domain.goals.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotpack.krocs.global.common.entity.Priority;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GoalCreateResponseDTO {
    private final Long goalId;

    private final String title;

    private final Priority priority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate endDate;

    private final Integer duration;

    private final boolean completed;

    private final List<SubGoalResponseDTO> subGoals;

    private final Integer completionPercentage;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime updatedAt;
}
