package com.jungle.Tabbit.domain.waiting.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "waiting_id")
    private Long waitingId;

    @Column(nullable = false, name = "people_number")
    private int peopleNumber;
    @Column(nullable = false, name = "waiting_number")
    private int waitingNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "waiting_status")
    private WaitingStatus waitingStatus;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public Waiting(int peopleNumber, int waitingNumber, Restaurant restaurant, WaitingStatus waitingStatus, Member member) {
        this.peopleNumber = peopleNumber;
        this.waitingNumber = waitingNumber;
        this.waitingStatus = waitingStatus;
        this.restaurant = restaurant;
        this.member = member;
    }
}