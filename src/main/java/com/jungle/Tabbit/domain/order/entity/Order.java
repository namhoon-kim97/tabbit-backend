package com.jungle.Tabbit.domain.order.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "orders")
@SQLDelete(sql = "UPDATE orders SET is_deleted = true WHERE order_id = ?")
@Where(clause = "is_deleted = false")
public class Order extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "waiting_id", nullable = false)
    private Waiting waiting;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Where(clause = "is_deleted = false")
    private final List<OrderMenu> orderMenus = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;  // soft delete flag

    public Order(Member member, Restaurant restaurant, Waiting waiting) {
        this.member = member;
        this.restaurant = restaurant;
        this.status = OrderStatus.ORDERED;
        this.waiting = waiting;
    }

    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    @PreRemove
    private void preRemove() {
        this.isDeleted = true;
        for (OrderMenu orderMenu : this.orderMenus) {
            orderMenu.setDeleted(true);
        }
    }
}
