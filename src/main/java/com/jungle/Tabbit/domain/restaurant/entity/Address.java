package com.jungle.Tabbit.domain.restaurant.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Address")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @Column(length = 255)
    private String sido;

    @Column(length = 255)
    private String sigungu;

    @Column(length = 255)
    private String eupmyeondong;

    @Column(length = 255)
    private String roadAddress;

    @Column(length = 255)
    private String streetAddress;

    @Column(length = 255)
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
