package com.hotpack.krocs.domain.templates.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.hotpack.krocs.domain.templates.domain.SubTemplate;
import com.hotpack.krocs.domain.templates.domain.Template;
import com.hotpack.krocs.domain.templates.dto.request.SubTemplateCreateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.request.SubTemplateRequestDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateCreateResponseDTO;
import com.hotpack.krocs.domain.templates.dto.response.SubTemplateResponseDTO;
import com.hotpack.krocs.global.common.entity.Priority;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

class SubTemplateConverterTest {

    @Mock
    private SubTemplateConverter subTemplateConverter;

    private SubTemplateCreateRequestDTO validCreateRequestDTO;
    private SubTemplateRequestDTO validSubTemplateRequestDTO;
    private SubTemplate validSubTemplate;
    private Template validTemplate;


    @BeforeEach
    void setup() {
        // given
        subTemplateConverter = new SubTemplateConverter();

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


    @Test
    @DisplayName("SubTemplateCreateRequestDTO를 List<SubTemplate>로 변환")
    void toEntityList_Success() {
        // when
        List<SubTemplate> subTemplates = subTemplateConverter.toEntityList(validTemplate,
            validCreateRequestDTO);

        // then
        assertThat(subTemplates.size()).isEqualTo(1);
        SubTemplate createdSubTemplate = subTemplates.getFirst();
        assertThat(createdSubTemplate.getTemplate()).isEqualTo(validSubTemplate.getTemplate());
        assertThat(createdSubTemplate.getTitle()).isEqualTo(validSubTemplate.getTitle());
    }

    @Test
    @DisplayName("SubTemplateRequestDTO를 SubTemplate로 변환")
    void toEntity_Success() {
        // when
        SubTemplate createdSubTemplate = subTemplateConverter.toEntity(validTemplate,
            validSubTemplateRequestDTO);

        // then
        assertThat(createdSubTemplate.getTemplate()).isEqualTo(validSubTemplate.getTemplate());
        assertThat(createdSubTemplate.getTitle()).isEqualTo(validSubTemplate.getTitle());
    }

    @Test
    @DisplayName("List<SubTemplate>를 SubTemplateCreateResponseDTO로 변환")
    void toCreateResponseDTO_Success() {
        // when
        SubTemplateCreateResponseDTO responseDTO = subTemplateConverter.toCreateResponseDTO(
            List.of(validSubTemplate));

        // then
        assertThat(responseDTO.getSubTemplates().size()).isEqualTo(1);
        SubTemplateResponseDTO subTemplateResponseDTO = responseDTO.getSubTemplates().getFirst();
        assertThat(subTemplateResponseDTO.getSubTemplateId()).isEqualTo(
            validSubTemplate.getSubTemplateId());
        assertThat(subTemplateResponseDTO.getTemplateId()).isEqualTo(
            validSubTemplate.getTemplate().getTemplateId());
        assertThat(subTemplateResponseDTO.getTitle()).isEqualTo(validSubTemplate.getTitle());
    }

    @Test
    @DisplayName("SubTemplate을 SubTemplateResponseDTO로 변환")
    void toResponseDTO_Success() {
        // when
        SubTemplateResponseDTO responseDTO = subTemplateConverter.toResponseDTO(
            validSubTemplate);

        // then
        assertThat(responseDTO.getSubTemplateId()).isEqualTo(
            validSubTemplate.getSubTemplateId());
        assertThat(responseDTO.getTemplateId()).isEqualTo(
            validSubTemplate.getTemplate().getTemplateId());
        assertThat(responseDTO.getTitle()).isEqualTo(validSubTemplate.getTitle());
    }

    @Test
    @DisplayName("List<SubTemplate>를 List<SubTemplateResponseDTO>로 변환")
    void toListResponseDTO_Success() {
        // when
        List<SubTemplateResponseDTO> responseDTOs = subTemplateConverter.toListResponseDTO(
            List.of(validSubTemplate));

        // then
        assertThat(responseDTOs).hasSize(1);
        assertThat(responseDTOs.getFirst().getSubTemplateId()).isEqualTo(
            validSubTemplate.getSubTemplateId());
        assertThat(responseDTOs.getFirst().getTemplateId()).isEqualTo(
            validSubTemplate.getTemplate().getTemplateId());
        assertThat(responseDTOs.getFirst().getTitle()).isEqualTo(validSubTemplate.getTitle());
    }

}