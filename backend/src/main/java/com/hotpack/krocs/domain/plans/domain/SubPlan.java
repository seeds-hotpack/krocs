package com.hotpack.krocs.domain.plans.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanUpdateRequestDTO;
import com.hotpack.krocs.global.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "sub_plans")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubPlan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_plan_id")
    private Long subPlanId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "is_completed", nullable = false)
    @Builder.Default
    private Boolean isCompleted = false;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public void updateFrom(SubPlanUpdateRequestDTO requestDTO, Boolean wasCompleted) {
        if (requestDTO.getTitle() != null) {
            this.title = requestDTO.getTitle();
        }

        if (requestDTO.getIsCompleted() != null) {
            this.isCompleted = requestDTO.getIsCompleted();

            if (!Boolean.TRUE.equals(wasCompleted) && Boolean.TRUE.equals(this.isCompleted)) {
                // 미완료 → 완료
                this.completedAt = LocalDateTime.now();
            } else if (Boolean.FALSE.equals(this.isCompleted)) {
                // 완료 → 미완료
                this.completedAt = null;
            }
        }
    }
}
