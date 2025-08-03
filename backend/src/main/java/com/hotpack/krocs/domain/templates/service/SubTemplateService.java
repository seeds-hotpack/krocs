package com.hotpack.krocs.domain.templates.service;

import com.hotpack.krocs.domain.templates.dto.request.SubTemplateCreateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateCreateResponseDTO;

public interface SubTemplateService {

  SubTemplateCreateResponseDTO createSubTemplates(Long templateId,
      SubTemplateCreateRequestDTO requestDTO,
      Long userId);
}
