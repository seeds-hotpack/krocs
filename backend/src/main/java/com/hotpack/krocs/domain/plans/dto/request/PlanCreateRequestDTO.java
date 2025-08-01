package com.hotpack.krocs.domain.plans.dto.request;

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

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @Builder.Default
    private Boolean allDay = false;

    @Min(value = 1, message = "에너지는 최소 1이어야 합니다")
    @Max(value = 10, message = "에너지는 최대 10이어야 합니다")
    @Builder.Default
    private Integer energy = 1;
}
