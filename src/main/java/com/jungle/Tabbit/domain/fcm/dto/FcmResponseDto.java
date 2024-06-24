package com.jungle.Tabbit.domain.fcm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FcmResponseDto {
    private boolean validateOnly;
    private FcmResponseDto .Message message;

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Message {
        private FcmResponseDto .Notification notification;
        private String token;
    }

    @Builder
    @AllArgsConstructor
    @Getter
    public static class Notification {
        private String title;
        private String body;
        private String image;
    }
}