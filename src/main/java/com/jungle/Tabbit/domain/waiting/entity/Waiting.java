package com.jungle.Tabbit.domain.waiting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Waiting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "waiting_id")
    private Long id;

    private int people_number;
    private int waiting_number;

    @Enumerated(EnumType.STRING)
    private WaitingStatus waitingStatus;
}
