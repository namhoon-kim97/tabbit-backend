package com.jungle.Tabbit.domain.order.entity;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "menu_category")
public class MenuCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_name", length = 255, nullable = false)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "category")
    private List<Menu> menuList = new ArrayList<>();

    public MenuCategory(String name, Restaurant restaurant) {
        this.categoryName = name;
        this.restaurant = restaurant;
    }

    public void update(String categoryName) {
        this.categoryName = categoryName;
    }
}
