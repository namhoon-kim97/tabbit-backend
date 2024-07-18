package com.jungle.Tabbit.domain.member.dto;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.entity.MemberRole;
import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Schema(description = "멤버 회원가입 응답 DTO")
public class MemberJoinRequestDto {
    @Pattern(regexp = "^[a-z0-9]{4,10}$", message = "Username must be between 4 and 10 characters long and contain only lowercase letters and digits.")
    @Schema(description = "멤버 아이디")
    private String username;
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]+${2,10}", message = "Nickname must be between 2 and 10 characters long and contain only letters and digits.")
    @Schema(description = "멤버 닉네임")
    private String nickname;
    @Pattern(regexp = "^[a-zA-Z0-9!@#$%^&*()]{8,15}$", message = "Password must be between 8 and 15 characters long and contain only letters and digits.")
    @Schema(description = "멤버 비밀번호")
    private String password;
    @Schema(description = "멤버 Role")
    private MemberRole memberRole;
    private String fcmToken;

    public Member createMember(PasswordEncoder passwordEncoder, Badge badge) {
        return new Member(nickname, passwordEncoder.encode(password), username, memberRole, badge, fcmToken);
    }
}
