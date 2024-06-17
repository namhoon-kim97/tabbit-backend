package com.jungle.example_code.domain.reply.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReplyRequestDto {
    @Getter
    @NoArgsConstructor
    public static class R_CREATE {

        private Long boardId;

        private String username;

        private String content;

        private String password;

    }

    @Getter
    @NoArgsConstructor
    public static class R_UPDATE {

        private String content;

        private String password;

    }

    @Getter
    @NoArgsConstructor
    public static class R_DELETE {

        private Long replyId;

        private String password;

    }

    @Getter
    @NoArgsConstructor
    public static class R_CONDITION {

        private Long boardId;

        private String username;

    }
}
