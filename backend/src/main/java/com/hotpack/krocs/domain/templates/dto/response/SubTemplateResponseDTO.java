package com.hotpack.krocs.domain.templates.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @Schema(
        description = "생성 일시",
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        example = "2025-08-03T15:05:12"
    )
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime createdAt;

    @Schema(
        description = "수정 일시",
        pattern = "yyyy-MM-dd'T'HH:mm:ss",
        example = "2025-08-03T15:05:12"
    )
    @JsonProperty("updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime updatedAt;
}
