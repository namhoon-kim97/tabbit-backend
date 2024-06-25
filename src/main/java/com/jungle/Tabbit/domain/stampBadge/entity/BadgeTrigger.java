package com.jungle.Tabbit.domain.stampBadge.entity;

import com.jungle.Tabbit.domain.restaurant.entity.Category;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "badgeTrigger")
public class BadgeTrigger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_trigger_id")
    private Long badgeTriggerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_cd")
    private Category category;

    @Column(name = "required_stamp_cnt")
    private Long requiredStampCnt;

    public BadgeTrigger(Badge badge, Category category, Long requiredStampCnt) {
        this.badge = badge;
        this.category = category;
        this.requiredStampCnt = requiredStampCnt;
    }
}
