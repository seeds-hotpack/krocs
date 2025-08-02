package com.hotpack.krocs.domain.templates.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class SubTemplateRequestDTO {

  @NotBlank(message = "목표 제목은 필수입니다")
  @Size(max = 200, message = "목표 제목은 200자를 초과할 수 없습니다")
  @Schema(description = "SubTemplate 생성 DTO", example = "퇴근하기")
  private String title;
}
