package com.hotpack.krocs.domain.plans.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hotpack.krocs.domain.goals.dto.request.SubGoalUpdateRequestDTO;
import com.hotpack.krocs.domain.plans.dto.request.SubPlanUpdateRequestDTO;
import com.hotpack.krocs.global.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
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

            // 완료 상태로 전환된 경우 completedAt 설정
            if (!Boolean.TRUE.equals(wasCompleted) && Boolean.TRUE.equals(this.isCompleted)) {
                this.completedAt = LocalDateTime.now();
            }
        }
    }
}
