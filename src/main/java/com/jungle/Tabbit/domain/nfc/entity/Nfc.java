package com.jungle.Tabbit.domain.nfc.entity;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import jakarta.persistence.*;

public class Nfc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "nfc_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
}
