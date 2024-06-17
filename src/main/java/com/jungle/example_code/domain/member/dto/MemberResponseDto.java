package com.jungle.example_code.domain.member.dto;


import com.jungle.example_code.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

public class MemberResponseDto {

    @Getter
    @Builder
    public static class READ {

        private Long userId;

        private String email;

        private String username;

        private String role;

        private String accessToken;

        public static READ of(Member member) {
            return READ.builder()
                    .userId(member.getId())
                    .email(member.getEmail())
                    .username(member.getUsername())
                    .role(String.valueOf(member.getRole()))
                    .build();
        }


    }

    @Getter
    public static class adminREAD {

        private Long userId;

        private String password;

        private String email;

        private String username;

        private String role;

    }
}
