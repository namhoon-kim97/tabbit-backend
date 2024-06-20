package com.jungle.Tabbit.domain.member.dto;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.entity.MemberRole;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
public class MemberJoinRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "Username must be between 4 and 10 characters long and contain only lowercase letters and digits.")
    private String username;
//    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "Nickname must be between 4 and 10 characters long and contain only lowercase letters and digits.") validation rule 같이 생각해줘요
    private String nickname;
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()]{8,15}$", message = "Password must be between 8 and 15 characters long and contain only letters and digits.")
    private String password;
    private MemberRole memberRole;

    public Member createMember(PasswordEncoder passwordEncoder) {
        return new Member(nickname, passwordEncoder.encode(password), username, memberRole, 1L);
    }
}
