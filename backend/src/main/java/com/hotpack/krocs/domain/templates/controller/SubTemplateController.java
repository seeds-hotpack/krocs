package com.hotpack.krocs.domain.templates.controller;

import com.hotpack.krocs.domain.templates.dto.response.SubTemplateDeleteResponseDTO;
import com.hotpack.krocs.domain.templates.exception.SubTemplateException;
import com.hotpack.krocs.domain.templates.exception.SubTemplateExceptionType;
import com.hotpack.krocs.domain.templates.service.SubTemplateService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/subtemplates")
public class SubTemplateController {

    private final SubTemplateService subTemplateService;

    @Operation(summary = "서브 템플릿 삭제", description = "subTemplateId를 조회하여 서브템플릿을 삭제합니다.")
    @DeleteMapping("/{subTemplateId}")
    public ApiResponse<SubTemplateDeleteResponseDTO> deleteSubTemplate(
        @PathVariable("subTemplateId") Long subTemplateId) {
        try {
            SubTemplateDeleteResponseDTO responseDTO = subTemplateService.deleteSubTemplate(
                subTemplateId);

            return ApiResponse.success(responseDTO);
        } catch (SubTemplateException e) {
            throw e;
        } catch (Exception e) {
            throw new SubTemplateException(SubTemplateExceptionType.SUB_TEMPLATE_DELETE_FAILED);
        }

    }
    
}
