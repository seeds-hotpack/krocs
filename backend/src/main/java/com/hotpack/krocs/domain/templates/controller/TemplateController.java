package com.hotpack.krocs.domain.templates.controller;


import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.exception.TemplateException;
import com.hotpack.krocs.domain.templates.service.TemplateService;
import com.hotpack.krocs.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 탬플릿 관련 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/templates")
public class TemplateController {

    private final TemplateService templateService;

    @PostMapping
    public ApiResponse<CreateTemplateResponseDTO> createTemplate(
            @Valid @RequestBody CreateTemplateRequestDTO requestDTO,
            @RequestParam(value = "user_id", required = false) Long userId
    ) {
        try{

            log.info("요청 들어옴: {}", requestDTO); // 여기 안찍히면 JSON 파싱에서 죽는 것
            CreateTemplateResponseDTO responseDTO = templateService.createTemplate(requestDTO, userId);
            return ApiResponse.success(responseDTO);

        } catch (TemplateException e){
            throw  e;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
