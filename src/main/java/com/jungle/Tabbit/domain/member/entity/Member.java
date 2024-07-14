package com.jungle.Tabbit.domain.member.entity;

import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberBadge;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "member")
@SQLDelete(sql = "UPDATE member SET is_deleted = true WHERE member_id = ?")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "member_id")
    private Long memberId;

    @Column(nullable = false, name = "nickname")
    private String nickname;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "username")
    private String username;

    @Column(nullable = false, name = "member_role")
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    @JoinColumn(nullable = false, name = "badge_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Badge badge;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberStamp> memberStampList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberBadge> memberBadgeList = new ArrayList<>();

    @Column(nullable = false, name = "fcm_token")
    private String fcmToken;

    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;


    public Member(String nickname, String password, String username, MemberRole memberRole, Badge badge, String fcmToken) {
        this.nickname = nickname;
        this.password = password;
        this.username = username;
        this.memberRole = memberRole;
        this.badge = badge;
        this.fcmToken = fcmToken;
    }
    public void updateMemberInfo(String nickname, Badge badge) {
        this.nickname = nickname;
        this.badge = badge;
    }

    public void updateMemberPassword(String password) {
        this.password = password;
    }

    public void updateFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public void deleteMember() {
        this.isDeleted = true;
        if (memberStampList != null) {
            memberStampList.forEach(MemberStamp::delete);
        }
        if (memberBadgeList != null) {
            memberBadgeList.forEach(MemberBadge ::delete);
        }
    }
}
