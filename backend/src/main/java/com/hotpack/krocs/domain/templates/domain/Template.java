package com.hotpack.krocs.domain.templates.domain;

import com.hotpack.krocs.global.common.entity.BaseTimeEntity;
import com.hotpack.krocs.global.common.entity.Priority;
import com.hotpack.krocs.global.common.entity.RepeatType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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
}