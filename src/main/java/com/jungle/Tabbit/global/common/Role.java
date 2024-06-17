package com.jungle.Tabbit.global.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Role {

        ROLE_ADMIN("ADMIN"),

        ROLE_USER("USER");

        String userRole;

        public static Role of(String userRole) {

            return Role.valueOf(userRole);
        }
}
