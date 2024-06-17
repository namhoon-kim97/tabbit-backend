package com.jungle.example_code.domain.board.service;


import com.jungle.example_code.domain.board.dto.BoardRequestDto;
import com.jungle.example_code.domain.board.dto.BoardResponseDto;
import com.jungle.example_code.domain.board.entity.Board;
import com.jungle.example_code.domain.member.entity.Member;
import com.jungle.example_code.domain.board.repository.BoardRepository;
import com.jungle.example_code.domain.member.repository.MemberRepository;
import com.jungle.example_code.global.common.Role;
import com.jungle.example_code.global.exception.WrongPasswordException;
import com.jungle.example_code.global.model.ResponseStatus;
import com.jungle.example_code.global.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public BoardResponseDto.READ createBoard(String username, BoardRequestDto.B_CREATE create) {

        Member member = memberRepository.findMemberByUsernameAndDelYn(username, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        Board board = new Board(create.getTitle(), create.getContent(), member
                , bCryptPasswordEncoder.encode(create.getPassword()), "N");

        boardRepository.save(board);

        return BoardResponseDto.READ.of(board);
    }

    @Override
    public BoardResponseDto.READ updateBoard(String username, Role role, Long id, BoardRequestDto.B_UPDATE update) {

        Member member = memberRepository.findMemberByUsernameAndDelYn(username, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        Board board = boardRepository.findBoardByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_BOARD_NOT_FOUND));

        if (!isAuthorizedToUpdateOrDelete(role, board, update.getPassword()))
            throw new WrongPasswordException(ResponseStatus.FAIL_BOARD_PASSWORD_NOT_MATCHED);

        board.updateBoard(update);

        return BoardResponseDto.READ.of(board);
    }

    @Override
    public void deleteBoard(Role role, Long id, BoardRequestDto.B_DELETE delete) {
        Board board = boardRepository.findBoardByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_BOARD_NOT_FOUND));

        if (!isAuthorizedToUpdateOrDelete(role, board, delete.getPassword()))
            throw new WrongPasswordException(ResponseStatus.FAIL_BOARD_PASSWORD_NOT_MATCHED);

        board.delete();
    }

    @Override
    public BoardResponseDto.READ getBoard(Long id) {
        Board board = boardRepository.findBoardByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_BOARD_NOT_FOUND));

        return BoardResponseDto.READ.of(board);
    }

    @Override
    public List<BoardResponseDto.READ> getAllBoards() {
        List<Board> boards = boardRepository.findAllByDelYn("N");

        return boards.stream()
                .map(BoardResponseDto.READ::of)
                .collect(Collectors.toList());
    }

    private boolean isAuthorizedToUpdateOrDelete(Role role, Board board, String password) {
        if (!(role.equals(Role.ROLE_ADMIN))) {
            return bCryptPasswordEncoder.matches(password, board.getPassword());
        }
        return true;
    }
}
