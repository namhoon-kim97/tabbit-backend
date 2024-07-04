package com.jungle.Tabbit.domain.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "menu_category")
public class MenuCategory {
    @Id
    @Column(name = "category_cd", length = 255, nullable = false)
    private String categoryCd;

    @Column(name = "category_name", length = 255, nullable = false)
    private String categoryName;

    public MenuCategory(String CategoryCd, String name) {
        this.categoryCd = CategoryCd;
        this.categoryName = name;
    }
}
