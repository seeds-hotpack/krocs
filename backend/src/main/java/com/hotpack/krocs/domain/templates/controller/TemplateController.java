package com.hotpack.krocs.domain.templates.controller;


import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.UpdateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateResponseDTO;
import com.hotpack.krocs.domain.templates.exception.TemplateException;
import com.hotpack.krocs.domain.templates.service.TemplateService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 탬플릿 관련 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;

    /**
     *
     * @param requestDTO
     * @param userId
     * @return
     */
    @Operation(summary = "템플릿 생성", description = "title, priority, duration을 설정해 템플릿을 생성합니다.")
    @PostMapping
    public ApiResponse<CreateTemplateResponseDTO> createTemplate(
            @Valid @RequestBody CreateTemplateRequestDTO requestDTO,
            @RequestParam(value = "user_id", required = false) Long userId
    ) {
        try{
            CreateTemplateResponseDTO responseDTO = templateService.createTemplate(requestDTO, userId);
            return ApiResponse.success(responseDTO);

        } catch (TemplateException e){
            throw  e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @param userId
     * @param title
     * @return
     */
    @Operation(summary = "템플릿 전체 조회 및 검색", description = "사용자 ID 기반으로 템플릿을 조회하며, title 키워드로 부분 검색이 가능합니다.")
    @GetMapping
    public ApiResponse<List<TemplateResponseDTO>> getTemplates(
            @RequestParam(value = "user_id") Long userId,
            @RequestParam(required = false) String title) {
        try {
            List<TemplateResponseDTO> responseDTO = templateService.getTemplatesByUserAndTitle(userId, title);
            return ApiResponse.success(responseDTO);

        } catch (TemplateException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 수정
     * @param templateId
     * @param userId
     * @param requestDTO
     * @return
     */
    @Operation(summary = "템플릿 수정", description = "탬플릿을 id 기준으로 수정합니다.")
    @PatchMapping("/{template_id}")
    public ApiResponse<TemplateResponseDTO> getTemplates(
            @PathVariable(value = "template_id") Long templateId,
            @RequestParam(value = "user_id") Long userId,
            @RequestBody UpdateTemplateRequestDTO requestDTO){
        try {
            TemplateResponseDTO responseDTO = templateService.updateTemplate(templateId, userId, requestDTO);
            return ApiResponse.success(responseDTO);

        } catch (TemplateException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(summary = "템플릿 삭제", description = "템플릿을 탬플릿ID로 삭제합니다.")
    @DeleteMapping("/{template_id}")
    public ApiResponse<Void> deleteTemplate(
            @PathVariable(value = "template_id") Long templateId,
            @RequestParam(value = "user_id") Long userId) {
        try {

            log.info("Template 호출: templateId={}, userId={}", templateId, userId);
            templateService.deleteTemplate(templateId, userId);
            return ApiResponse.success(null);

        } catch (TemplateException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
