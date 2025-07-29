package com.hotpack.krocs.domain.templates.dto.request;

import com.hotpack.krocs.global.common.entity.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class TemplateCreateRequestDTO {
    @NotBlank(message = "목표 제목은 필수입니다")
    @Size(max = 200, message = "목표 제목은 200자를 초과할 수 없습니다")
    private String title;

    @Builder.Default
    private Priority priority = Priority.MEDIUM;

    @NotNull(message = "목표 기간은 필수입니다")
    @Positive(message = "목표 기간은 1일 이상이어야 합니다")
    private Integer duration;
}

