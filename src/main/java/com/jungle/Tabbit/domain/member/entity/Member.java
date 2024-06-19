package com.jungle.Tabbit.domain.member.entity;

import com.jungle.example_code.global.common.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "Member")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(nullable = false)
    private Long BadgeId;

    public Member(String nickname, String password, String username, Role role, Long badgeId) {
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.role = role;
        BadgeId = badgeId;
    }
}
