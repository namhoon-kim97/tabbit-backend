package com.jungle.Tabbit.domain.member.dto;

import com.jungle.Tabbit.domain.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberLoginDto extends MemberLoginResponseDto{
    private String token;

    @Builder
    public MemberLoginDto(String nickname, MemberRole memberRole, String token) {
        super(nickname, memberRole);
        this.token = token;
    }

    public MemberLoginResponseDto toResponseDto() {
        return new MemberLoginResponseDto(getNickname(), getMemberRole());
    }
}
