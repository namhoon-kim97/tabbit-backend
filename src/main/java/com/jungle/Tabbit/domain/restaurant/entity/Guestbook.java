package com.jungle.Tabbit.domain.restaurant.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.global.common.EarnedTimestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "guestbook")
public class Guestbook extends EarnedTimestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private Long guestbookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @Column(name = "content", nullable = false, length = 255)
    private String content;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    public Guestbook(Member member, Restaurant restaurant, String content, String imageUrl) {
        this.member = member;
        this.restaurant = restaurant;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
