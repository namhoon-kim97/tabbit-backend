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
    @Column(name = "restaurant_detail_id")
    private Long restaurantDetailId;

    @Column(name = "opening_hours", length = 255)
    private String openingHours;

    @Column(name = "break_time", length = 255)
    private String breakTime;

    @Column(name = "holidays", length = 255)
    private String holidays;

    @Column(name = "restaurant_number", length = 255)
    private String restaurantNumber;

    @Column(name = "description", length = 255)
    private String description;

    public RestaurantDetail(String openingHours, String breakTime, String holidays, String restaurantNumber, String description) {
        this.openingHours = openingHours;
        this.breakTime = breakTime;
        this.holidays = holidays;
        this.restaurantNumber = restaurantNumber;
        this.description = description;
    }
}
