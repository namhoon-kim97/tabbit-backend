package com.jungle.example_code.domain.member.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.jungle.example_code.global.common.Role;

public class MemberRequestDto {

    @Getter
    @NoArgsConstructor
    public static class M_CREATE {

        private String email;

        private String password;

        private String username;

        private Role role;

    }

    @Getter
    @NoArgsConstructor
    public static class M_UPDATE {

        private String password;

        private String username;

        private String role;

    }

    @Getter
    @NoArgsConstructor
    public static class M_CONDITION {

        private String email;

        private String username;

        private String role;
    }

    @Getter
    @NoArgsConstructor
    public static class LOGIN {

        private String email;

        private String password;

    }

    @Getter
    @NoArgsConstructor
    public static class M_DELETE {

        private String email;

        private String password;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class M_FIND {

        private String email;

    }
}
