package com.jungle.example_code.domain.board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class BoardRequestDto {
    @Getter
    @NoArgsConstructor
    public static class B_CREATE {

        private String title;

        private String content;

        private String password;

        private String username;

    }

    @Getter
    @NoArgsConstructor
    public static class B_UPDATE {

        private String title;

        private String content;

        private String password;

    }

    @Getter
    @NoArgsConstructor
    public static class B_CONDITION {

        private String title;

        private String content;

        private String username;
    }

    @Getter
    @NoArgsConstructor
    public static class B_DELETE {

        private Long boardId;

        private String password;

    }

}
