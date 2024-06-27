package com.jungle.Tabbit.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class FcmData {
    private String target;
    private String messageType;
    private String restaurantId;
    private String restaurantName;
    private String waitingNumber;


}
