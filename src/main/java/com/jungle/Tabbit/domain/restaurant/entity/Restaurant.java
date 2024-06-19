package com.jungle.Tabbit.domain.restaurant.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.global.common.Timestamped;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Restaurant")
public class Restaurant extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long restaurantId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_detail_id", nullable = false)
    private RestaurantDetail restaurantDetail;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member owner;

    @Column(nullable = false, length = 255)
    private String name;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_cd", nullable = false)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(nullable = false, precision = 13, scale = 10)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 13, scale = 10)
    private BigDecimal longitude;

    @Column(nullable = false)
    @ColumnDefault("10")
    private Long estimatedTimePerTeam = 10L;

    public Restaurant(RestaurantDetail restaurantDetail, Member owner, String name, Category category, Address address, BigDecimal latitude, BigDecimal longitude, Long estimatedTimePerTeam) {
        this.restaurantDetail = restaurantDetail;
        this.owner = owner;
        this.name = name;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estimatedTimePerTeam = estimatedTimePerTeam;
    }
}
