package com.jungle.example_code.domain.reply.service;


import com.jungle.example_code.domain.reply.dto.ReplyRequestDto;
import com.jungle.example_code.domain.reply.dto.ReplyResponseDto;
import com.jungle.example_code.global.common.Role;

public interface ReplyService {
    public ReplyResponseDto.READ createReply(String username, ReplyRequestDto.R_CREATE create);

    public ReplyResponseDto.READ updateReply(String username, Role role, Long id, ReplyRequestDto.R_UPDATE update);

    public void deleteReply(Role role, Long id, ReplyRequestDto.R_DELETE delete);

}
