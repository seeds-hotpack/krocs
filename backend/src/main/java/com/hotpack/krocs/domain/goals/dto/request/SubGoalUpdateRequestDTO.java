package com.hotpack.krocs.domain.goals.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
public record SubGoalUpdateRequestDTO(
    String title,
    @JsonProperty("is_completed")
    Boolean isCompleted
) {

}
