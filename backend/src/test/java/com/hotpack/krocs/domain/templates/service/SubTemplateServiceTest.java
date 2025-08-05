package com.hotpack.krocs.domain.templates.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.hotpack.krocs.domain.templates.converter.SubTemplateConverter;
import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.SubTemplateCreateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.SubTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateCreateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.exception.SubTemplateException;
import com.hotpack.krocs.domain.templates.exception.SubTemplateExceptionType;
import com.hotpack.krocs.domain.templates.facade.SubTemplateRepositoryFacade;
import com.hotpack.krocs.domain.templates.facade.TemplateRepositoryFacade;
import com.hotpack.krocs.global.common.entity.Priority;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SubTemplateServiceTest {

    @Spy
    private SubTemplateConverter subTemplateConverter;
    @Mock
    private SubTemplateRepositoryFacade subTemplateRepositoryFacade;
    @Mock
    private TemplateRepositoryFacade templateRepositoryFacade;

    @InjectMocks
    private SubTemplateServiceImpl subTemplateService;

    private SubTemplateCreateRequestDTO validCreateRequestDTO;
    private SubTemplateRequestDTO validSubTemplateRequestDTO;
    private SubTemplate validSubTemplate;
    private Template validTemplate;


    @BeforeEach
    void setup() {
        // given
        validTemplate = Template.builder()
            .templateId(1L)
            .priority(Priority.HIGH)
            .duration(10)
            .build();

        validSubTemplateRequestDTO = SubTemplateRequestDTO.builder()
            .title("테스트입니다")
            .build();

        validCreateRequestDTO = SubTemplateCreateRequestDTO.builder()
            .subTemplates(List.of(validSubTemplateRequestDTO))
            .build();

        validSubTemplate = SubTemplate.builder()
            .subTemplateId(1L)
            .template(validTemplate)
            .title("테스트입니다")
            .build();
    }

    // ======= CREATE =======

    @Test
    @DisplayName("서브 템플릿 생성 성공")
    void createSubTemplates_Success() {
        // when
        when(templateRepositoryFacade.findByTemplateId(1L)).thenReturn(validTemplate);
        when(subTemplateRepositoryFacade.saveAll(any())).thenReturn(List.of(validSubTemplate));

        SubTemplateCreateResponseDTO responseDTO = subTemplateService.createSubTemplates(1L,
            validCreateRequestDTO, 1L);

        // then
        SubTemplateResponseDTO subTemplateResponseDTO = responseDTO.getSubTemplates().getFirst();
        assertThat(subTemplateResponseDTO.getSubTemplateId()).isEqualTo(1L);
        assertThat(subTemplateResponseDTO.getTemplateId()).isEqualTo(1L);
        assertThat(subTemplateResponseDTO.getTitle()).isEqualTo("테스트입니다");
    }

    @Test
    @DisplayName("서브 템플릿 생성 실패 - templateId가 null인 경우")
    void createSubTemplates_templateIdIsNull() {
        // when
        SubTemplateException exception = assertThrows(SubTemplateException.class,
            () -> subTemplateService.createSubTemplates(null, validCreateRequestDTO, 1L));

        // then
        assertThat(exception.getSubTemplateExceptionType()).isEqualTo(
            SubTemplateExceptionType.SUB_TEMPLATE_TEMPLATE_ID_IS_NULL);
    }

    @Test
    @DisplayName("서브 템플릿 생성 실패 - template가 null인 경우")
    void createSubTemplates_templateIsNull() {
        // when
        when(templateRepositoryFacade.findByTemplateId(1L)).thenReturn(null);

        SubTemplateException exception = assertThrows(SubTemplateException.class,
            () -> subTemplateService.createSubTemplates(1L, validCreateRequestDTO, 1L));

        // then
        assertThat(exception.getSubTemplateExceptionType()).isEqualTo(
            SubTemplateExceptionType.SUB_TEMPLATE_TEMPLATE_NOT_FOUND);
    }

    @Test
    @DisplayName("서브 템플릿 생성 실패 - 예상치 못한 오류 발생")
    void createSubTemplates_UnknownException() {
        // when
        when(templateRepositoryFacade.findByTemplateId(1L)).thenThrow(new RuntimeException());

        SubTemplateException exception = assertThrows(SubTemplateException.class,
            () -> subTemplateService.createSubTemplates(1L, validCreateRequestDTO, 1L));

        // then
        assertThat(exception.getSubTemplateExceptionType()).isEqualTo(
            SubTemplateExceptionType.SUB_TEMPLATE_CREATION_FAILED);
    }

    @Test
    @DisplayName("서브 템플릿 전체 조회 - 성공")
    void getSubTemplates_Success() {
        // when
        when(templateRepositoryFacade.findByTemplateId(1L)).thenReturn(validTemplate);
        when(subTemplateRepositoryFacade.findBySubTemplate(validTemplate)).thenReturn(
            List.of(validSubTemplate));
        List<SubTemplateResponseDTO> subTemplateResponseDTOs = subTemplateService.getSubTemplates(
            1L);

        // then
        assertThat(subTemplateResponseDTOs).hasSize(1);
        assertThat(subTemplateResponseDTOs.getFirst().getSubTemplateId()).isEqualTo(1L);
        assertThat(subTemplateResponseDTOs.getFirst().getTemplateId()).isEqualTo(1L);
        assertThat(subTemplateResponseDTOs.getFirst().getTitle()).isEqualTo("테스트입니다");
    }

    @Test
    @DisplayName("서브 템플릿 전체 조회 - templateId가 null인 경우")
    void getSubTemplates_TemplateIdIsNull() {

        // when & then
        assertThatThrownBy(() -> subTemplateService.getSubTemplates(null))
            .isInstanceOf(SubTemplateException.class)
            .satisfies(ex -> {
                SubTemplateException e = (SubTemplateException) ex;
                assertThat(e.getSubTemplateExceptionType()).isEqualTo(
                    SubTemplateExceptionType.SUB_TEMPLATE_TEMPLATE_ID_IS_NULL);
            });
    }

    @Test
    @DisplayName("서브 템플릿 전체 조회 - subTemplate을 찾지 못한 경우")
    void getSubTemplates_SubTemplateNotFound() {
        when(templateRepositoryFacade.findByTemplateId(1L)).thenReturn(validTemplate);
        when(subTemplateRepositoryFacade.findBySubTemplate(validTemplate)).thenReturn(null);
        // when & then
        assertThatThrownBy(() -> subTemplateService.getSubTemplates(1L))
            .isInstanceOf(SubTemplateException.class)
            .satisfies(ex -> {
                SubTemplateException e = (SubTemplateException) ex;
                assertThat(e.getSubTemplateExceptionType()).isEqualTo(
                    SubTemplateExceptionType.SUB_TEMPLATE_FOUND_FAILED);
            });
    }
}