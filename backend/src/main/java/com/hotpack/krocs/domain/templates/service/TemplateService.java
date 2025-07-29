package com.hotpack.krocs.domain.templates.service;

import com.hotpack.krocs.domain.templates.dto.request.TemplateCreateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.TemplateUpdateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateCreateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateResponseDTO;

import java.util.List;

public interface TemplateService {

    TemplateCreateResponseDTO createTemplate(TemplateCreateRequestDTO requestDTO, Long userId);

    List<TemplateResponseDTO> getTemplatesByUserAndTitle(Long userId, String title);

    TemplateResponseDTO updateTemplate(Long templateId, Long userId, TemplateUpdateRequestDTO requestDTO);

    void deleteTemplate(Long templateId, Long userId);

}
