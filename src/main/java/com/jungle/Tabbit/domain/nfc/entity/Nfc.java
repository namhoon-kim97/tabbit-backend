package com.jungle.Tabbit.domain.nfc.entity;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Nfc")
public class Nfc {
    @Id
    @Column(nullable = false, name = "nfc_id")
    private String nfcId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public Nfc(String nfcId, Restaurant restaurant) {
        this.nfcId = nfcId;
        this.restaurant = restaurant;
    }
}
