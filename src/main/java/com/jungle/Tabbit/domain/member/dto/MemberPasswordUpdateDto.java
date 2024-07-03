package com.jungle.Tabbit.domain.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
@Schema(description = "멤버 패스워드 수정 요청 DTO")
public class MemberPasswordUpdateDto {
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()]{8,15}$", message = "Password must be between 8 and 15 characters long and contain only letters and digits.")
    private String password;
}
