package com.jungle.Tabbit.domain.member.entity;

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

    @Column(nullable = false, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @Column(nullable = false, name = "badge_id")
    private Long BadgeId;

    public Member(String nickname, String password, String username, MemberRole memberRole, Long badgeId) {
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.memberRole = memberRole;
        BadgeId = badgeId;
    }
}
