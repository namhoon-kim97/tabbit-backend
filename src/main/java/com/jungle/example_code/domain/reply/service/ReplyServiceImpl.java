package com.jungle.example_code.domain.reply.service;


import com.jungle.example_code.domain.board.entity.Board;
import com.jungle.example_code.domain.board.repository.BoardRepository;
import com.jungle.example_code.domain.member.entity.Member;
import com.jungle.example_code.domain.member.repository.MemberRepository;
import com.jungle.example_code.domain.reply.dto.ReplyRequestDto;
import com.jungle.example_code.domain.reply.dto.ReplyResponseDto;
import com.jungle.example_code.domain.reply.entity.Reply;
import com.jungle.example_code.domain.reply.repository.ReplyRepository;
import com.jungle.example_code.global.common.Role;
import com.jungle.example_code.global.exception.WrongPasswordException;
import com.jungle.example_code.global.exception.NotFoundException;
import com.jungle.example_code.global.model.ResponseStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ReplyServiceImpl implements ReplyService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ReplyRepository replyRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public ReplyResponseDto.READ createReply(String username, ReplyRequestDto.R_CREATE create) {
        Member member = memberRepository.findMemberByUsernameAndDelYn(username, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        Board board = boardRepository.findBoardByIdAndDelYn(create.getBoardId(), "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_BOARD_NOT_FOUND));

        Reply reply = new Reply(member, board, create.getContent(),
                bCryptPasswordEncoder.encode(create.getPassword()), "N");

        replyRepository.save(reply);

        return ReplyResponseDto.READ.of(reply);
    }

    @Override
    public ReplyResponseDto.READ updateReply(String username, Role role, Long id, ReplyRequestDto.R_UPDATE update) {


        Member member = memberRepository.findMemberByUsernameAndDelYn(username, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        Reply reply = replyRepository.findReplyByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_REPLY_NOT_FOUND));

        if (!isAuthorizedToUpdateOrDelete(role, reply, update.getPassword()))
            throw new WrongPasswordException(ResponseStatus.FAIL_BOARD_PASSWORD_NOT_MATCHED);

        reply.updateReply(update);

        return ReplyResponseDto.READ.of(reply);
    }

    @Override
    public void deleteReply(Role role, Long id, ReplyRequestDto.R_DELETE delete) {

        Reply reply = replyRepository.findReplyByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_REPLY_NOT_FOUND));

        if (!isAuthorizedToUpdateOrDelete(role, reply, delete.getPassword()))
            throw new WrongPasswordException(ResponseStatus.FAIL_BOARD_PASSWORD_NOT_MATCHED);

        reply.delete();
    }

    private boolean isAuthorizedToUpdateOrDelete(Role role, Reply reply, String password) {
        if (!(role.equals(Role.ROLE_ADMIN))) {
            return bCryptPasswordEncoder.matches(password, reply.getPassword());
        }
        return true;
    }
}
