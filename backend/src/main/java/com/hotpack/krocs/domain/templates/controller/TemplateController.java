package com.hotpack.krocs.domain.templates.controller;


import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
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

    @Operation(summary = "템플릿 조회 및 검색", description = "사용자 ID 기반으로 템플릿을 조회하며, title 키워드로 부분 검색이 가능합니다.")
    @GetMapping
    public ApiResponse<List<TemplateResponseDTO>> getTemplates(
            @RequestParam Long user_id,
            @RequestParam(required = false) String title) {
        try {
            List<TemplateResponseDTO> responseDTO = templateService.getTemplatesByUserAndTitle(user_id, title);
            return ApiResponse.success(responseDTO);

        } catch (TemplateException e) {
            throw e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
