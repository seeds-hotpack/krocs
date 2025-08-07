package com.hotpack.krocs.domain.plans.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanUpdateRequestDTO {
    private String title;

    @Schema(
        description = "시작 일시",
        pattern = "yyyy-MM-dd'T'HH:mm",
        example = "2025-08-03T15:05"
    )
    @JsonProperty("start_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDateTime;

    @Schema(
        description = "종료 일시",
        pattern = "yyyy-MM-dd'T'HH:mm",
        example = "2025-08-03T15:05"
    )
    @JsonProperty("end_date_time")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endDateTime;

    @JsonProperty("all_day")
    private Boolean allDay;

    @JsonProperty("is_completed")
    private Boolean isCompleted;
}
