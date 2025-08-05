package com.hotpack.krocs.domain.templates.converter;

import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.SubTemplateCreateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.SubTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateCreateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateResponseDTO;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class SubTemplateConverter {

    public List<SubTemplate> toEntityList(Template template,
        SubTemplateCreateRequestDTO requestDTO) {
        List<SubTemplate> subTemplates = new ArrayList<>();
        for (SubTemplateRequestDTO subTemplateRequestDTO : requestDTO.getSubTemplates()) {
            subTemplates.add(toEntity(template, subTemplateRequestDTO));
        }
        return subTemplates;
    }

    public SubTemplate toEntity(Template template, SubTemplateRequestDTO requestDTO) {
        return SubTemplate.builder()
            .title(requestDTO.getTitle())
            .template(template)
            .build();
    }

    public SubTemplateCreateResponseDTO toCreateResponseDTO(
        List<SubTemplate> subTemplates) {
        List<SubTemplateResponseDTO> subTemplateResponseDTOs = new ArrayList<>();
        for (SubTemplate subTemplate : subTemplates) {
            subTemplateResponseDTOs.add(toResponseDTO(subTemplate));
        }

        return SubTemplateCreateResponseDTO.builder()
            .subTemplates(subTemplateResponseDTOs)
            .build();
    }

    public List<SubTemplateResponseDTO> toListResponseDTO(List<SubTemplate> subTemplates) {
        List<SubTemplateResponseDTO> subTemplateResponseDTOs = new ArrayList<>();
        for (SubTemplate subTemplate : subTemplates) {
            subTemplateResponseDTOs.add(toResponseDTO(subTemplate));
        }
        return subTemplateResponseDTOs;
    }

    public SubTemplateResponseDTO toResponseDTO(SubTemplate subTemplate) {
        return SubTemplateResponseDTO.builder()
            .subTemplateId(subTemplate.getSubTemplateId())
            .templateId(subTemplate.getTemplate().getTemplateId())
            .title(subTemplate.getTitle())
            .createdAt(subTemplate.getCreatedAt())
            .updatedAt(subTemplate.getUpdatedAt())
            .build();
    }
}
