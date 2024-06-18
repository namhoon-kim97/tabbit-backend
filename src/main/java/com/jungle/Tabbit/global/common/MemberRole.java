package com.jungle.Tabbit.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MemberRole {

        ROLE_ADMIN("ADMIN"),

        ROLE_USER("USER"),
        ROLE_MANAGER("MANAGER");

        final String userRole;

        public static MemberRole of(String userRole) {

            return MemberRole.valueOf(userRole);
        }
}
