package com.jungle.Tabbit.domain.stampBadge.entity;

import com.jungle.Tabbit.domain.restaurant.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BadgeTrigger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_trigger_id")
    private Long badgeTriggerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_cd", nullable = false)
    private Category category;

    @Column(name = "required_stamp_cnt", nullable = false)
    private Long requiredStampCnt;

    @Column(name = "trigger_type", nullable = false)
    private String triggerType;

    @Column(name = "trigger_condition", nullable = false)
    private String condition;

    public BadgeTrigger(Badge badge, Category category, Long requiredStampCnt, String triggerType, String condition) {
        this.badge = badge;
        this.category = category;
        this.requiredStampCnt = requiredStampCnt;
        this.triggerType = triggerType;
        this.condition = condition;
    }
}
