package com.jungle.Tabbit.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "멤버 수정 요청 DTO")
public class MemberUpdateRequestDto {

    @Schema(description = "멤버 닉네임")
    private String nickname;

    @Schema(description = "멤버 비밀번호")
    private String password;

    @Schema(description = "멤버 칭호 ID")
    private Long badgeId;

}
