package com.hotpack.krocs.domain.goals.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotpack.krocs.global.common.entity.Priority;
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
public class GoalCreateRequestDTO {

  @NotBlank(message = "목표 제목은 필수입니다")
  @Size(max = 200, message = "목표 제목은 200자를 초과할 수 없습니다")
  private String title;

  @Builder.Default
  private Priority priority = Priority.MEDIUM;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate startDate;

  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate endDate;

  @NotNull(message = "목표 기간은 필수입니다")
  @Positive(message = "목표 기간은 1일 이상이어야 합니다")
  private Integer duration;
}

//private void validateGoalCreation(CreateGoalRequestDTO requestDTO) {
//
//  if (requestDTO.getTitle().length() > 200) {
//    throw new GoalException(GoalExceptionType.GOAL_TITLE_TOO_LONG);
//  }
//
//  // 기간 검증
//  if (requestDTO.getDuration() == null || requestDTO.getDuration() <= 0) {
//    throw new GoalException(GoalExceptionType.GOAL_DURATION_INVALID);
//  }
//
//  // 날짜 범위 검증
//  if (requestDTO.getStartDate() != null && requestDTO.getEndDate() != null) {
//    if (requestDTO.getStartDate().isAfter(requestDTO.getEndDate())) {
//      throw new GoalException(GoalExceptionType.INVALID_GOAL_DATE_RANGE);
//    }
//  }
//}
