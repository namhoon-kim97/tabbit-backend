package com.jungle.Tabbit.domain.member.dto;

import com.jungle.Tabbit.domain.member.entity.MemberRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@Schema(description = "멤버 로그인 응답 DTO")
@AllArgsConstructor
public class MemberLoginResponseDto {
    @Schema(description = "멤버 닉네임")
    private String nickname;
    @Schema(description = "멤버 Role")
    private MemberRole memberRole;
}
