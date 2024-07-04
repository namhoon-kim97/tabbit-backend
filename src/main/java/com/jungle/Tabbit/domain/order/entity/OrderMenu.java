package com.jungle.Tabbit.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "order_menu")
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id", nullable = false)
    private Menu menu;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public OrderMenu(Order order, Menu menu, int quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }
}
