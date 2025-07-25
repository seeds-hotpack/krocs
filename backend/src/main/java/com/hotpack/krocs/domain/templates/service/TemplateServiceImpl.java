package com.hotpack.krocs.domain.templates.service;


import com.hotpack.krocs.domain.templates.converter.TemplateConverter;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.UpdateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateResponseDTO;
import com.hotpack.krocs.domain.templates.exception.TemplateException;
import com.hotpack.krocs.domain.templates.exception.TemplateExceptionType;
import com.hotpack.krocs.domain.templates.facade.TemplateRepositoryFacade;
import com.hotpack.krocs.domain.templates.validator.TemplateValidator;
import com.hotpack.krocs.global.common.entity.Priority;
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
     * 생성
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

            if (!result.validTitleEmpty()) {
                throw new TemplateException(TemplateExceptionType.TEMPLATE_TITLE_EMPTY);
            }
            if (!result.validTitleTooLong()){
                throw new TemplateException(TemplateExceptionType.TEMPLATE_TITLE_TOO_LONG);
            }
            if (!result.validDuration()) {
                throw new TemplateException(TemplateExceptionType.TEMPLATE_DURATION_INVALID);
            }

            // 변환 및 저장
            Template template = templateConverter.toEntity(requestDTO);
            Template savedTemplate = templateRepositoryFacade.save(template);

            return templateConverter.toCreateResponseDTO(savedTemplate);
        } catch (TemplateException e) {
            throw e;
        } catch (Exception e) {
            log.error("탬플릿 생성 중 예상치 못한 오류 발생: {}", e.getMessage(), e);
            throw new TemplateException(TemplateExceptionType.TEMPLATE_CREATION_FAILED);
        }
    }


    /**
     * 조회 / 검색(title)
     * @param userId 사용자 ID (나중에 토큰으로 대체)
     * @param title 검색할 내용
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public List<TemplateResponseDTO> getTemplatesByUserAndTitle(Long userId, String title) {
        try {
            List<Template> templates;

            if (!templateValidator.isTitleEmpty(title) && !templateValidator.isTitleTooLong(title)) {
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


    /**
     *
     * @param templateId 탬플릿 id
     * @param userId 사용자 ID
     * @param requestDTO 업데이트할 dto
     * @return
     */
    @Transactional
    @Override
    public TemplateResponseDTO updateTemplate(Long templateId, Long userId, UpdateTemplateRequestDTO requestDTO) {
        try {

            Template template = templateRepositoryFacade.findByTemplateId(templateId)
                    .orElseThrow(() -> new TemplateException(TemplateExceptionType.TEMPLATE_NOT_FOUND));

            applyUpdates(template, requestDTO);
            return templateConverter.toTemplateResponseDTO(template);

        } catch (TemplateException e) {
            throw e;
        } catch (Exception e) {
            log.error("템플릿 수정 중 예외 발생: {}", e.getMessage(), e);
            throw new TemplateException(TemplateExceptionType.TEMPLATE_UPDATE_FAILED);
        }
    }

    // 유효성 검사
    private void applyUpdates(Template template, UpdateTemplateRequestDTO requestDTO){
        if (requestDTO.title() != null) {
            if (templateValidator.isTitleEmpty(requestDTO.title())) {
                throw new TemplateException(TemplateExceptionType.TEMPLATE_TITLE_EMPTY);
            }
            if (templateValidator.isTitleTooLong(requestDTO.title())) {
                throw new TemplateException(TemplateExceptionType.TEMPLATE_TITLE_TOO_LONG);
            }
            template.setTitle(requestDTO.title());
        }

        if (requestDTO.priority() != null) {
            if(!templateValidator.isValidPriority(requestDTO.priority())){
                throw new TemplateException(TemplateExceptionType.TEMPLATE_INVALID_PRIORITY);
            }
            template.setPriority(Priority.valueOf(requestDTO.priority()));
        }

        if (requestDTO.duration() != null) {
            if(!templateValidator.isValidDuration(requestDTO.duration())){
                throw new TemplateException(TemplateExceptionType.TEMPLATE_DURATION_INVALID);
            }
            template.setDuration(requestDTO.duration());
        }
    }

    /**
     * 삭제
     * @param templateId
     * @param userId
     */
    @Transactional
    @Override
    public void deleteTemplate(Long templateId, Long userId) {
        log.info("Template 호출: templateId={}, userId={}", templateId, userId);
        Template template = templateRepositoryFacade.findByTemplateId(templateId)
                .orElseThrow(() -> new TemplateException(TemplateExceptionType.TEMPLATE_NOT_FOUND));


        try {
            templateRepositoryFacade.delete(template);
        } catch (TemplateException e) {
            throw e;
        } catch (Exception e) {
            log.error("템플릿 삭제 중 예외 발생: {}", e.getMessage(), e);
            throw new TemplateException(TemplateExceptionType.TEMPLATE_DELETE_FAILED);
        }
    }

}
