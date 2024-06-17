package com.jungle.example_code.domain.board.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.jungle.example_code.domain.board.entity.Board;
import com.jungle.example_code.domain.reply.dto.ReplyResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class BoardResponseDto {

    @Getter
    @Builder
    public static class READ {
        private Long boardId;

        private String username;

        private String title;

        private String content;

        private List<ReplyResponseDto.READ> replies;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdAt;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime updatedAt;

        public static READ of(Board board) {
            List<ReplyResponseDto.READ> replies = board.getReplies().stream()
                    .map(ReplyResponseDto.READ::of)
                    .collect(Collectors.toList());

            return READ.builder()
                    .boardId(board.getId())
                    .username(board.getMember().getUsername())
                    .title(board.getTitle())
                    .content(board.getContent())
                    .replies(replies)
                    .createdAt(board.getCreatedAt())
                    .updatedAt(board.getUpdatedAt())
                    .build();
        }
    }
}
