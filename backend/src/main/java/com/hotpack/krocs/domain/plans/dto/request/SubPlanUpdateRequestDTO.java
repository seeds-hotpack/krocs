package com.hotpack.krocs.domain.plans.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SubPlanUpdateRequestDTO {

    private String title;

    @JsonProperty("is_completed")
    private Boolean isCompleted;
}
