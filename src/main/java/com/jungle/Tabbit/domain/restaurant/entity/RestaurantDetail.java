package com.jungle.Tabbit.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "restaurant_detail")
public class RestaurantDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_detail_id")
    private Long restaurantDetailId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

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

    public RestaurantDetail(Restaurant restaurant, String openingHours, String breakTime, String holidays, String restaurantNumber, String description) {
        this.restaurant = restaurant;
        this.openingHours = openingHours;
        this.breakTime = breakTime;
        this.holidays = holidays;
        this.restaurantNumber = restaurantNumber;
        this.description = description;
    }

    public void update(String openingHours, String breakTime, String holidays, String restaurantNumber, String description) {
        this.openingHours = openingHours;
        this.breakTime = breakTime;
        this.holidays = holidays;
        this.restaurantNumber = restaurantNumber;
        this.description = description;
    }
}
