package com.jungle.Tabbit.domain.stampBadge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "칭호를 가진 유저 DTO")
public class MemberBadgeResponseDto {
    @Schema(description = "회원 ID", example = "1")
    private Long memberId;

    @Schema(description = "회원 닉네임", example = "nickname")
    private String nickname;

    @Schema(description = "칭호 이름", example = "Super User")
    private String badgeName;

    public static MemberBadgeResponseDto of(Long memberId, String nickname, String badgeName) {
        return MemberBadgeResponseDto.builder()
                .memberId(memberId)
                .nickname(nickname)
                .badgeName(badgeName)
                .build();
    }
}
