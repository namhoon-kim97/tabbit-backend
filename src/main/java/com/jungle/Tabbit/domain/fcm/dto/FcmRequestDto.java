package com.jungle.Tabbit.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
@AllArgsConstructor
public class FcmRequestDto {
    private String token;
    private String title;
    private String body;
    private FcmData data;
    private boolean dataOnly;
}