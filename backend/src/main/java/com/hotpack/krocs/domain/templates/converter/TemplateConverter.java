package com.hotpack.krocs.domain.templates.converter;

import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.TemplateCreateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateCreateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TemplateConverter {

    public Template toEntity(TemplateCreateRequestDTO requestDTO) {
        return Template.builder()
                .title(requestDTO.getTitle())
                .priority(requestDTO.getPriority() != null ? requestDTO.getPriority() : Priority.MEDIUM)
                .duration(requestDTO.getDuration())
                .subTemplates(List.of()) // 초기 생성 시 빈 리스트 (추후 추가 가능)
                .build();
    }

    public TemplateCreateResponseDTO toCreateResponseDTO(Template template) {
        List<SubTemplateResponseDTO> subTemplateDTOs = template.getSubTemplates() != null ?
                template.getSubTemplates().stream()
                        .map(this::toSubTemplateResponseDTO)
                        .collect(Collectors.toList()) :
                List.of();

        return TemplateCreateResponseDTO.builder()
                .templateId(template.getTemplateId())
                .title(template.getTitle())
                .priority(template.getPriority())
                .duration(template.getDuration())
                .subTemplates(subTemplateDTOs)
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .build();
    }

    private SubTemplateResponseDTO toSubTemplateResponseDTO(SubTemplate subTemplate) {
        return SubTemplateResponseDTO.builder()
                .subTemplateId(subTemplate.getSubTemplateId())
                .title(subTemplate.getTitle())
                .build();
    }


    public TemplateResponseDTO toTemplateResponseDTO(Template template) {
        return TemplateResponseDTO.builder()
                .templateId(template.getTemplateId())
                .title(template.getTitle())
                .priority(template.getPriority())
                .duration(template.getDuration())
                .createdAt(template.getCreatedAt())
                .updatedAt(template.getUpdatedAt())
                .subTemplates(template.getSubTemplates().stream()
                        .map(this::toSubTemplateResponseDTO)
                        .toList())
                .build();
    }
}