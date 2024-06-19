package com.jungle.Tabbit.domain.restaurant.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Guestbook")
public class Guestbook extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guestbookId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    @Column(nullable = false, length = 255)
    private String content;

    @Column(nullable = false, length = 255)
    private String imageUrl;

    public Guestbook(Member member, Restaurant restaurant, String content, String imageUrl) {
        this.member = member;
        this.restaurant = restaurant;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}
