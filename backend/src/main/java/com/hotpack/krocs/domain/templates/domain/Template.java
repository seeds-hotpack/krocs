package com.hotpack.krocs.domain.templates.domain;

import com.hotpack.krocs.domain.templates.dto.request.TemplateUpdateRequestDTO;
import com.hotpack.krocs.global.common.entity.BaseTimeEntity;
import com.hotpack.krocs.global.common.entity.Priority;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "templates")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "template_id")
  private Long templateId;

  @Column(name = "title", nullable = false, length = 200)
  private String title;

  @Enumerated(EnumType.STRING)
  @Column(name = "priority", nullable = false, length = 10)
  @Builder.Default
  private Priority priority = Priority.MEDIUM;

  @Column(name = "duration", nullable = false)
  private Integer duration;

  @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<SubTemplate> subTemplates;

  public void updateFrom(TemplateUpdateRequestDTO requestDTO) {

    if (requestDTO.getTitle() != null) {
      this.title = requestDTO.getTitle();
    }

    if (requestDTO.getDuration() != null) {
      this.duration = requestDTO.getDuration();
    }

    if (requestDTO.getPriority() != null) {
      this.priority = requestDTO.getPriority();
    }
    
  }
}