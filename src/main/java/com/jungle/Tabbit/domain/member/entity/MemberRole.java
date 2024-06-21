package com.jungle.Tabbit.domain.member.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {
    ROLE_ADMIN("ADMIN"),
    ROLE_MANAGER("MANAGER"),
    ROLE_USER("USER");

    String memberRole;

    public static MemberRole of(String userRole) {
        return MemberRole.valueOf(userRole);
    }
}
