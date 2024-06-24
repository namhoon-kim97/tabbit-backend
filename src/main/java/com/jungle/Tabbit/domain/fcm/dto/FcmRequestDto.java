package com.jungle.Tabbit.domain.fcm.dto;

import lombok.*;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FcmRequestDto {
    private String token;
    private String title;
    private String body;

    @Builder
    public FcmRequestDto(String token, String title, String body) {
        this.token = token;
        this.title = title;
        this.body = body;
    }
}