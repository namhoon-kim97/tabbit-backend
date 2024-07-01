package com.jungle.Tabbit.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "멤버 로그인 요청DTO")
public class MemberLoginRequestDto {
    @Schema(description = "멤버 아이디")
    private String username;
    @Schema(description = "멤버 비밀번호")
    private String password;

    @Schema(description = "FCM 토큰")
    private String fcmToken;
}
