package com.hotpack.krocs.domain.templates.domain;

import com.hotpack.krocs.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_templates")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubTemplate extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_template_id")
    private Long subTemplateId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private Template template;

    @Column(name = "title", nullable = false, length = 200)
    private String title;


}