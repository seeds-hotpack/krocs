package com.hotpack.krocs.domain.templates.converter;

import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateConverter {

    /**
     * CreateTemplateRequestDTO를 Template 엔티티로 변환합니다.
     *
     * @param requestDTO 템플릿 생성 요청 DTO
     * @return Template 엔티티
     */
    public Template toEntity(CreateTemplateRequestDTO requestDTO) {
        return Template.builder()
                .title(requestDTO.getTitle())
                .priority(requestDTO.getPriority() != null ? requestDTO.getPriority() : Priority.MEDIUM)
                .duration(requestDTO.getDuration())
                .subTemplates(List.of()) // 초기 생성 시 빈 리스트 (추후 추가 가능)
                .build();
    }

    /**
     * Template 엔티티를 CreateTemplateResponseDTO로 변환합니다.
     *
     * @param template Template 엔티티
     * @return CreateTemplateResponseDTO
     */
    public CreateTemplateResponseDTO toCreateResponseDTO(Template template) {
        List<SubTemplateResponseDTO> subTemplateDTOs = template.getSubTemplates() != null ?
                template.getSubTemplates().stream()
                        .map(this::toSubTemplateResponseDTO)
                        .collect(Collectors.toList()) :
                List.of();

        return CreateTemplateResponseDTO.builder()
                .templateId(template.getTemplateId())
                .title(template.getTitle())
                .priority(template.getPriority())
                .duration(template.getDuration())
                .subTemplates(subTemplateDTOs)
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }

    /**
     * SubTemplate 엔티티를 SubTemplateResponseDTO로 변환합니다.
     *
     * @param subTemplate SubTemplate 엔티티
     * @return SubTemplateResponseDTO
     */
    private SubTemplateResponseDTO toSubTemplateResponseDTO(SubTemplate subTemplate) {
        return SubTemplateResponseDTO.builder()
                .subTemplateId(subTemplate.getSubTemplateId())
                .title(subTemplate.getTitle())
                .build();
    }
}