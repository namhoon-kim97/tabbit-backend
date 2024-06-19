package com.jungle.Tabbit.domain.waiting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WaitingStatus {
    STATUS_WAITING("WAITING"),

    STATUS_CALLED("CALLED"),
    STATUS_SEATED("SEATED"),
    STATUS_NOSHOW("NOSHOW");

    final String waitingStatus;

    public static WaitingStatus of(String waitingStatus) {

        return WaitingStatus.valueOf(waitingStatus);
    }
}
