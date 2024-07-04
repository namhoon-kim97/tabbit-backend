package com.jungle.Tabbit.domain.order.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "order")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderMenu> orderItems;

    public Order(Member member, Restaurant restaurant, List<OrderMenu> orderItems) {
        this.member = member;
        this.restaurant = restaurant;
        this.orderItems = orderItems;
    }
}
