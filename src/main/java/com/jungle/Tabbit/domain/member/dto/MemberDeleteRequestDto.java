package com.jungle.Tabbit.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "멤버 삭제 요청 DTO")
public class MemberDeleteRequestDto {

    @Schema(description = "멤버 비밀번호")
    private String password;




}
