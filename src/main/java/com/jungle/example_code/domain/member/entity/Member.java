package com.jungle.example_code.domain.member.entity;

import com.jungle.example_code.domain.member.dto.MemberRequestDto;
import com.jungle.example_code.global.common.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "member_id")
    private Long id;

    @Column(nullable = false, name = "email", unique = true)
    private String email;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "username", unique = true)
    private String username;

    @Column(nullable = false, name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false, name = "del_yn")
    private String delYn;

    @Builder
    public Member(String email, String password, String username, Role role, String delYn) {
        this.email = email;
        this.password = password;
        this.username = username;
        this.role = role;
        this.delYn = delYn;
    }


    public void updateName(MemberRequestDto.M_UPDATE update) {
        this.username = update.getUsername();
    }

    public void updatePassword(MemberRequestDto.M_UPDATE update) {
        this.password = update.getPassword();
    }

    public void updateRole(MemberRequestDto.M_UPDATE update) {
        this.role = Role.valueOf(update.getRole());
    }

    public void delete() {
        this.delYn = "Y";
    }

}
