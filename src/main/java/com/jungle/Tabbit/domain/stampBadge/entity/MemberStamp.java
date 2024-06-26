package com.jungle.Tabbit.domain.stampBadge.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.global.common.EarnedTimestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "memberStamp")
public class MemberStamp extends EarnedTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stamp_id")
    private Long stampId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "visit_count", nullable = false)
    private Long visitCount = 1L;

    public MemberStamp(Member member, Restaurant restaurant) {
        this.member = member;
        this.restaurant = restaurant;
    }

    public void updateVisitCount(Long visitCount) {
        this.visitCount = visitCount + 1;
    }
}
