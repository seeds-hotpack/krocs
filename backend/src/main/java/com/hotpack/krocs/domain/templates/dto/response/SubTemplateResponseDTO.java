package com.hotpack.krocs.domain.templates.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubTemplateResponseDTO {

  @JsonProperty("sub_template_id")
  private final Long subTemplateId;

  @JsonProperty("template_id")
  private final Long templateId;

  private final String title;

  @JsonProperty("created_at")
  private final LocalDateTime createdAt;

  @JsonProperty("updated_at")
  private final LocalDateTime updatedAt;
}
