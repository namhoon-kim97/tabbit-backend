package com.jungle.example_code.domain.reply.entity;


import com.jungle.example_code.domain.board.entity.Board;
import com.jungle.example_code.domain.member.entity.Member;
import com.jungle.example_code.domain.reply.dto.ReplyRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "reply_id")
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @Column(nullable = false, name = "password")
    private String password;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(nullable = false, name = "del_yn")
    private String delYn;

    @Builder
    public Reply(Member member, Board board, String content, String password, String delYn) {
        this.member = member;
        this.board = board;
        this.content = content;
        this.delYn = delYn;
        this.createdAt = LocalDateTime.now();
        this.password = password;
    }

    public void updateReply(ReplyRequestDto.R_UPDATE update) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }

    public void delete() {
        this.delYn = "Y";
    }
}
