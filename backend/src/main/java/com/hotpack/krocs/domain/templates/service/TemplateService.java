package com.hotpack.krocs.domain.templates.service;

import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;

public interface TemplateService {

    /**
     *
     * @param requestDTO 탬플릿 생성 요청 데이터
     * @param userId 사용자 ID
     * @return 생성된 탬플릿 정보
     */
    CreateTemplateResponseDTO createTemplate(CreateTemplateRequestDTO requestDTO, Long userId);
}
