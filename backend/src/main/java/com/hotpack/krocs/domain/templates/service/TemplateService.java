package com.hotpack.krocs.domain.templates.service;

import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.UpdateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateResponseDTO;

import java.util.List;

public interface TemplateService {

    /**
     *
     * @param requestDTO 탬플릿 생성 요청 데이터
     * @param userId 사용자 ID
     * @return 생성된 탬플릿 정보
     */
    CreateTemplateResponseDTO createTemplate(CreateTemplateRequestDTO requestDTO, Long userId);

    /**
     *
     * @param userId 사용자 ID (나중에 토큰으로 대체)
     * @param title 검색할 내용
     * @return 검색한 템플릿 정보
     */
    List<TemplateResponseDTO> getTemplatesByUserAndTitle(Long userId, String title);


    /**
     *
     * @param templateId 탬플릿 id
     * @param userId 사용자 ID
     * @param requestDTO 업데이트할 dto
     * @return 업데이트 완료한 template
     */
    TemplateResponseDTO updateTemplate(Long templateId, Long userId, UpdateTemplateRequestDTO requestDTO);

}
