package com.jungle.Tabbit.domain.restaurant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Category")
public class Category {
    @Id
    @Column(length = 255, nullable = false)
    private String categoryCd;

    @Column(length = 255)
    private String categoryName;

    public Category(String categoryCd, String categoryName) {
        this.categoryCd = categoryCd;
        this.categoryName = categoryName;
    }
}
