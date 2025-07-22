package com.hotpack.krocs.domain.goals.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotpack.krocs.common.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CreateGoalRequestDTO {
    @NotBlank(message = "목표 제목은 필수입니다")
    @Size(max = 200, message = "목표 제목은 200자를 초과할 수 없습니다")
    private String title;

    private Priority priority;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @NotNull(message = "목표 기간은 필수입니다")
    @Positive(message = "목표 기간은 1일 이상이어야 합니다")
    private Integer duration;
}
