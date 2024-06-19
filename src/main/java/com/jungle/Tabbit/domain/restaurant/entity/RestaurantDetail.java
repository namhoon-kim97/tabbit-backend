package com.jungle.Tabbit.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Restaurant_detail")
public class RestaurantDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurant_detail_id;

    @Column(length = 255)
    private String openingHours;

    @Column(length = 255)
    private String breakTime;

    @Column(length = 255)
    private String holidays;

    @Column(length = 255)
    private String restaurantNumber;

    @Column(length = 255)
    private String description;

    public RestaurantDetail(String openingHours, String breakTime, String holidays, String restaurantNumber, String description) {
        this.openingHours = openingHours;
        this.breakTime = breakTime;
        this.holidays = holidays;
        this.restaurantNumber = restaurantNumber;
        this.description = description;
    }
}
