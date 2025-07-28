package com.hotpack.krocs.domain.goals.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SubGoalListResponseDTO {

  private List<SubGoalResponseDTO> subGoals;
}
