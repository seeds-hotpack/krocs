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

  public List<SubTemplate> toSubTemplateEntityList(Template template,
      SubTemplateCreateRequestDTO requestDTO) {
    List<SubTemplate> subTemplates = new ArrayList<>();
    for (SubTemplateRequestDTO subTemplateRequestDTO : requestDTO.getSubTemplates()) {
      subTemplates.add(toSubTemplateEntity(template, subTemplateRequestDTO));
    }
    return subTemplates;
  }

  public SubTemplate toSubTemplateEntity(Template template, SubTemplateRequestDTO requestDTO) {
    return SubTemplate.builder()
        .title(requestDTO.getTitle())
        .template(template)
        .build();
  }

  public SubTemplateCreateResponseDTO toSubTemplateCreateResponseDTO(
      List<SubTemplate> subTemplates) {
    List<SubTemplateResponseDTO> subTemplateResponseDTOs = new ArrayList<>();
    for (SubTemplate subTemplate : subTemplates) {
      subTemplateResponseDTOs.add(toSubTemplateResponseDTO(subTemplate));
    }

    return SubTemplateCreateResponseDTO.builder()
        .subTemplates(subTemplateResponseDTOs)
        .build();
  }

  public SubTemplateResponseDTO toSubTemplateResponseDTO(SubTemplate subTemplate) {
    return SubTemplateResponseDTO.builder()
        .subTemplateId(subTemplate.getSubTemplateId())
        .templateId(subTemplate.getSubTemplateId())
        .title(subTemplate.getTitle())
        .createdAt(subTemplate.getCreatedAt())
        .updatedAt(subTemplate.getUpdatedAt())
        .build();
  }
}
