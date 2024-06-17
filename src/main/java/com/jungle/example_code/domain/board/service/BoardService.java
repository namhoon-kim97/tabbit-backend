package com.jungle.example_code.domain.board.service;



import com.jungle.example_code.domain.board.dto.BoardRequestDto;
import com.jungle.example_code.domain.board.dto.BoardResponseDto;
import com.jungle.example_code.global.common.Role;

import java.util.List;

public interface BoardService {

    BoardResponseDto.READ createBoard(String username, BoardRequestDto.B_CREATE create);

    public BoardResponseDto.READ updateBoard(String username, Role role, Long id, BoardRequestDto.B_UPDATE update);

    public void deleteBoard(Role role, Long id, BoardRequestDto.B_DELETE delete);

    public BoardResponseDto.READ getBoard(Long id);

    public List<BoardResponseDto.READ> getAllBoards();

}
