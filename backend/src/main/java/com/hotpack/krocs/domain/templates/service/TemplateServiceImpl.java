package com.hotpack.krocs.domain.templates.service;


import com.hotpack.krocs.domain.templates.converter.TemplateConverter;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateResponseDTO;
import com.hotpack.krocs.domain.templates.exception.TemplateException;
import com.hotpack.krocs.domain.templates.exception.TemplateExceptionType;
import com.hotpack.krocs.domain.templates.facade.TemplateRepositoryFacade;
import com.hotpack.krocs.domain.templates.validator.TemplateValidator;
import com.hotpack.krocs.global.common.response.exception.handler.TemplateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepositoryFacade templateRepositoryFacade;
    private final TemplateConverter templateConverter;
    private final TemplateValidator templateValidator;


    /**
     *
     * @param requestDTO 탬플릿 생성 요청 데이터
     * @param userId 사용자 ID
     * @return
     */
    @Override
    @Transactional
    public CreateTemplateResponseDTO createTemplate(CreateTemplateRequestDTO requestDTO, Long userId) {
        try {
            // 유효성 검증
            TemplateValidator.TemplateValidationResult result = templateValidator.validateTemplateCreation(requestDTO);

            if (!result.validTitle()) {
                throw new TemplateException(TemplateExceptionType.TEMPLATE_TITLE_EMPTY);
            }
            if (!result.validDuration()) {
                throw new TemplateException(TemplateExceptionType.TEMPLATE_DURATION_INVALID);
            }

            // 변환 및 저장
            Template template = templateConverter.toEntity(requestDTO);
            Template savedTemplate = templateRepositoryFacade.saveTemplate(template);

            return templateConverter.toCreateResponseDTO(savedTemplate);
        } catch (TemplateException e) {
            throw e;
        } catch (Exception e) {
            log.error("탬플릿 생성 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new TemplateException(TemplateExceptionType.TEMPLATE_CREATION_FAILED);
        }
    }


    @Override
    @Transactional(readOnly = true)
    public List<TemplateResponseDTO> getTemplatesByUserAndTitle(Long userId, String title) {
        try {
            List<Template> templates;

            if (title != null && !title.isBlank()) {
                templates = templateRepositoryFacade.findByTitle(title);
            } else {
                templates = templateRepositoryFacade.findAll();
            }

            return templates.stream()
                    .map(templateConverter::toTemplateResponseDTO)
                    .toList();

        } catch (TemplateException e) {
            throw e;
        } catch (Exception e) {
            log.error("템플릿 조회 중 예외 발생: {}", e.getMessage(), e);
            throw new TemplateException(TemplateExceptionType.TEMPLATE_FOUND_FAILED);
        }
    }

}
