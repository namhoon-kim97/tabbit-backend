package com.jungle.Tabbit.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "sido", length = 255)
    private String sido;

    @Column(name = "sigungu", length = 255)
    private String sigungu;

    @Column(name = "eupmyeondong", length = 255)
    private String eupmyeondong;

    @Column(name = "road_address", length = 255)
    private String roadAddress;

    @Column(name = "street_address", length = 255)
    private String streetAddress;

    @Column(name = "detail_address", length = 255)
    private String detailAddress;

    public Address(String sido, String sigungu, String eupmyeondong, String roadAddress, String streetAddress, String detailAddress) {
        this.sido = sido;
        this.sigungu = sigungu;
        this.eupmyeondong = eupmyeondong;
        this.roadAddress = roadAddress;
        this.streetAddress = streetAddress;
        this.detailAddress = detailAddress;
    }
}
