package com.hotpack.krocs.domain.templates.service;


import com.hotpack.krocs.domain.templates.converter.TemplateConverter;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.CreateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.UpdateTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.CreateTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.TemplateResponseDTO;
import com.hotpack.krocs.domain.templates.exception.TemplateException;
import com.hotpack.krocs.domain.templates.exception.TemplateExceptionType;
import com.hotpack.krocs.domain.templates.facade.TemplateRepositoryFacade;
import com.hotpack.krocs.domain.templates.validator.TemplateValidator;
import com.hotpack.krocs.global.common.entity.Priority;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceTest {

    @Mock
    private TemplateRepositoryFacade templateRepositoryFacade;

    @Mock
    private TemplateValidator templateValidator;

    @Mock
    private TemplateConverter templateConverter;

    @InjectMocks
    private TemplateServiceImpl templateService;

    private CreateTemplateRequestDTO validCreateRequestDTO;
    private UpdateTemplateRequestDTO validUpdateRequestDTO;
    private Template validTemplate;
    private CreateTemplateResponseDTO validCreateResponseDTO;
    private TemplateResponseDTO validResponseDTO;
    private SubTemplateResponseDTO subTemplateResponseDTO;

    @BeforeEach
    void setUp() {
        validCreateRequestDTO = CreateTemplateRequestDTO.builder()
                .title("공부 루틴")
                .priority(Priority.HIGH)
                .duration(30)
                .build();

        validUpdateRequestDTO = UpdateTemplateRequestDTO.builder()
                .title("공부 루틴")
                .priority("HIGH")
                .duration(30)
                .build();

        validTemplate = Template.builder()
                .templateId(1L)
                .title("공부 루틴")
                .priority(Priority.HIGH)
                .duration(30)
                .subTemplates(new ArrayList<>())
                .build();

        validCreateResponseDTO = CreateTemplateResponseDTO.builder()
                .templateId(1L)
                .title("공부 루틴")
                .priority(Priority.HIGH)
                .duration(30)
                .build();

        validResponseDTO = TemplateResponseDTO.builder()
                .templateId(1L)
                .title("공부 루틴")
                .priority(Priority.HIGH)
                .duration(30)
                .subTemplates(Collections.emptyList())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    // ======= CREATE =======


    @Test
    @DisplayName("템플릿 생성 성공 테스트")
    void createTemplate_Success() {
        // given
        when(templateConverter.toEntity(validCreateRequestDTO)).thenReturn(validTemplate);
        when(templateRepositoryFacade.save(validTemplate)).thenReturn(validTemplate);
        when(templateConverter.toCreateResponseDTO(validTemplate)).thenReturn(validCreateResponseDTO);

        // when

        when(templateValidator.validateTemplateCreation(validCreateRequestDTO))
                .thenReturn(new TemplateValidator.TemplateValidationResult(true, true, true));
        CreateTemplateResponseDTO result = templateService.createTemplate(validCreateRequestDTO, 2L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("공부 루틴");
        assertThat(result.getDuration()).isEqualTo(30);
        assertThat(result.getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    @DisplayName("DTO 기본값 테스트 - priority는 명시하지 않으면 MEDIUM으로 설정된다")
    void createTemplateRequestDTO_DefaultPriority_ShouldBeMedium() {
        // given
        CreateTemplateRequestDTO result = CreateTemplateRequestDTO.builder()
                .title("공부 루틴")
                .duration(30)
                .build();

        // then
        assertThat(result.getPriority()).isEqualTo(Priority.MEDIUM);
    }

    @Test
    @DisplayName("템플릿 생성 실패 - 제목이 비어있는 경우")
    void createTemplate_Fail_BlankTitle() {
        // given
        CreateTemplateRequestDTO request = CreateTemplateRequestDTO.builder()
                .title("")
                .priority(Priority.MEDIUM)
                .duration(10)
                .build();

        TemplateValidator.TemplateValidationResult invalidResult =
                new TemplateValidator.TemplateValidationResult(false, true, true); // 제목 비어있음 (false)

        when(templateValidator.validateTemplateCreation(request)).thenReturn(invalidResult);

        // when & then
        TemplateException exception = assertThrows(TemplateException.class,
                () -> templateService.createTemplate(request, 1L));

        assertThat(exception.getTemplateExceptionType()).isEqualTo(TemplateExceptionType.TEMPLATE_TITLE_EMPTY);
    }

    @Test
    @DisplayName("템플릿 생성 실패 - duration이 음수인 경우")
    void createTemplate_Fail_NegativeDuration() {
        // given
        CreateTemplateRequestDTO request = CreateTemplateRequestDTO.builder()
                .title("공부")
                .priority(Priority.HIGH)
                .duration(-10)
                .build();

        TemplateValidator.TemplateValidationResult invalidResult =
                new TemplateValidator.TemplateValidationResult(true, true, false); // 제목 비어있음 (false)

        when(templateValidator.validateTemplateCreation(request)).thenReturn(invalidResult);

        // when & then
        TemplateException exception = assertThrows(TemplateException.class,
                () -> templateService.createTemplate(request, 1L));

        assertThat(exception.getTemplateExceptionType()).isEqualTo(TemplateExceptionType.TEMPLATE_DURATION_INVALID);
    }


    // ======= READ =======
    @Test
    @DisplayName("템플릿 전체 조회 성공 - 제목 없이 조회")
    void getTemplates_Success_WithoutTitle() {
        // given
        when(templateValidator.isTitleEmpty(null)).thenReturn(true);

        when(templateRepositoryFacade.findAll())
                .thenReturn(List.of(validTemplate));

        when(templateConverter.toTemplateResponseDTO(validTemplate))
                .thenReturn(validResponseDTO);

        // when
        List<TemplateResponseDTO> result = templateService.getTemplatesByUserAndTitle(1L, null);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("공부 루틴");
    }

    @Test
    @DisplayName("템플릿 검색 조회 성공 - 제목 키워드 포함")
    void getTemplates_Success_WithKeyword() {
        // when
        when(templateRepositoryFacade.findByTitle("공부"))
                .thenReturn(List.of(validTemplate));

        when(templateConverter.toTemplateResponseDTO(validTemplate))
                .thenReturn(validResponseDTO);

        // then
        List<TemplateResponseDTO> result = templateService.getTemplatesByUserAndTitle(1L, "공부");
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).contains("공부");
    }

    @Test
    @DisplayName("템플릿 검색 조회 - 결과가 없는 경우")
    void getTemplates_EmptyResult() {
        // when
        when(templateRepositoryFacade.findByTitle("운동"))
                .thenReturn(List.of());

        // then
        List<TemplateResponseDTO> result = templateService.getTemplatesByUserAndTitle(1L, "운동");
        assertThat(result).isEmpty();
    }


    // ======= UPDATE =======

    @Test
    @DisplayName("템플릿 수정 성공")
    void updateTemplate_Success() {
        // given
        Template existingTemplate = Template.builder()
                .templateId(1L)
                .title("기존 루틴")
                .priority(Priority.LOW)
                .duration(15)
                .subTemplates(new ArrayList<>()) // 기존 서브템플릿은 비워두고
                .build();

        UpdateTemplateRequestDTO requestDTO = UpdateTemplateRequestDTO.builder()
                .title("수정된 루틴")
                .priority("HIGH")
                .duration(40)
                .build();

        // when
        when(templateRepositoryFacade.findByTemplateId(1L)).thenReturn(Optional.of(existingTemplate));

        when(templateValidator.isValidPriority("HIGH")).thenReturn(true);
        when(templateValidator.isTitleEmpty("수정된 루틴")).thenReturn(false);
        when(templateValidator.isTitleTooLong("수정된 루틴")).thenReturn(false);
        when(templateValidator.isValidDuration(40)).thenReturn(true);

        // then
        assertThatCode(() -> templateService.updateTemplate(1L, 1L, requestDTO))
                .doesNotThrowAnyException();

        assertThat(existingTemplate.getTitle()).isEqualTo("수정된 루틴");
        assertThat(existingTemplate.getPriority()).isEqualTo(Priority.HIGH);
        assertThat(existingTemplate.getDuration()).isEqualTo(40);
    }

    @Test
    @DisplayName("템플릿 수정 실패 - duration이 최대값(200) 초과")
    void updateTemplate_Fail_DurationExceedsLimit() {
        // given
        Template existingTemplate = Template.builder()
                .templateId(1L)
                .title("기존 루틴")
                .priority(Priority.MEDIUM)
                .duration(30)
                .subTemplates(List.of())
                .build();

        UpdateTemplateRequestDTO requestDTO = UpdateTemplateRequestDTO.builder()
                .duration(201) // 최대 허용값 초과
                .build();

        // when

        when(templateValidator.isValidDuration(201)).thenReturn(false);
        when(templateRepositoryFacade.findByTemplateId(1L)).thenReturn(Optional.of(existingTemplate));

        TemplateException exception = catchThrowableOfType(
                () -> templateService.updateTemplate(1L, 1L, requestDTO),
                TemplateException.class
        );

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getTemplateExceptionType()).isEqualTo(TemplateExceptionType.TEMPLATE_DURATION_INVALID);
    }

    @Test
    @DisplayName("템플릿 수정 실패 - 존재하지 않는 템플릿")
    void updateTemplate_Fail_TemplateNotFound() {
        // when
        when(templateRepositoryFacade.findByTemplateId(1L))
                .thenReturn(Optional.empty());

        TemplateException exception = catchThrowableOfType(
                () -> templateService.updateTemplate(1L, 1L, validUpdateRequestDTO),
                TemplateException.class
        );

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getTemplateExceptionType()).isEqualTo(TemplateExceptionType.TEMPLATE_NOT_FOUND);
    }


    // D - 템플릿 삭제 성공
    @Test
    @DisplayName("템플릿 삭제 성공")
    void deleteTemplate_Success() {
        // when
        when(templateRepositoryFacade.findByTemplateId(1L))
                .thenReturn(Optional.of(validTemplate));

        // then
        assertThatCode(() -> templateService.deleteTemplate(1L, 1L))
                .doesNotThrowAnyException();

        verify(templateRepositoryFacade).delete(validTemplate);
    }

    // D - 템플릿 삭제 실패 (존재하지 않는 템플릿)
    @Test
    @DisplayName("템플릿 삭제 실패 - 존재하지 않는 템플릿")
    void deleteTemplate_Fail_TemplateNotFound() {
        // when
        when(templateRepositoryFacade.findByTemplateId(1L))
                .thenReturn(Optional.empty());


        TemplateException exception = catchThrowableOfType(
                () -> templateService.deleteTemplate(1L, 1L),
                TemplateException.class
        );

        // then
        assertThat(exception).isNotNull();
        assertThat(exception.getTemplateExceptionType()).isEqualTo(TemplateExceptionType.TEMPLATE_NOT_FOUND);
    }


}