package com.hotpack.krocs.domain.plans.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanCreateRequestDTO {
    @NotBlank(message = "일정 제목은 필수입니다")
    @Size(max = 200, message = "일정 제목은 200자를 초과할 수 없습니다")
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
    @Builder.Default
    private Boolean allDay = false;
}
