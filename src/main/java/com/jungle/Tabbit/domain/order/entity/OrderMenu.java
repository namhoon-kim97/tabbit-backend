package com.jungle.Tabbit.domain.order.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "order_menu")
@SQLDelete(sql = "UPDATE order_menu SET is_deleted = true WHERE order_menu_id = ?")
@Where(clause = "is_deleted = false")
public class OrderMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_menu_id")
    private Long orderMenuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;

    public OrderMenu(Order order, Menu menu, int quantity) {
        this.order = order;
        this.menu = menu;
        this.quantity = quantity;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}
