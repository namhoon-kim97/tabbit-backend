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
@Entity(name = "restaurant")
public class Restaurant extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_id")
    private Long restaurantId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_detail_id", nullable = false)
    private RestaurantDetail restaurantDetail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_cd", nullable = false)
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;

    @Column(name = "latitude", nullable = false, precision = 13, scale = 10)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 13, scale = 10)
    private BigDecimal longitude;

    @Column(name = "estimated_time_per_team", nullable = false)
    @ColumnDefault("10")
    private Long estimatedTimePerTeam = 10L;

    public Restaurant(RestaurantDetail restaurantDetail, Member member, String name, Category category, Address address, BigDecimal latitude, BigDecimal longitude, Long estimatedTimePerTeam) {
        this.restaurantDetail = restaurantDetail;
        this.member = member;
        this.name = name;
        this.category = category;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estimatedTimePerTeam = estimatedTimePerTeam;
    }

    public void updateEstimateTime(Long estimatedTimePerTeam) {
        this.estimatedTimePerTeam = estimatedTimePerTeam;
    }

    public void update(String name, Category category, BigDecimal latitude, BigDecimal longitude, Long estimatedTimePerTeam) {
        this.name = name;
        this.category = category;
        this.latitude = latitude;
        this.longitude = longitude;
        this.estimatedTimePerTeam = estimatedTimePerTeam;
    }
}
