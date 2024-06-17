package com.jungle.example_code.domain.board.controller;


import com.jungle.example_code.domain.board.dto.BoardRequestDto;
import com.jungle.example_code.domain.board.service.BoardService;
import com.jungle.example_code.global.auth.jwt.CustomUserDetails;
import com.jungle.example_code.global.model.CommonResponse;
import com.jungle.example_code.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/create")
    public CommonResponse<?> createBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody BoardRequestDto.B_CREATE create) {

        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE
                , boardService.createBoard(userDetails.getUsername(), create));
    }

    @PutMapping("/update/{id}")
    public CommonResponse<?> updateBoard(@AuthenticationPrincipal CustomUserDetails userDetails,
                                         @PathVariable Long id,
                                         @RequestBody BoardRequestDto.B_UPDATE update) {

        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE
                , boardService.updateBoard(userDetails.getUsername(), userDetails.getRole(), id, update));
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<?> deleteBoard(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id, @RequestBody BoardRequestDto.B_DELETE delete) {
        boardService.deleteBoard(userDetails.getRole(), id, delete);
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @GetMapping("/{id}")
    public CommonResponse<?> getBoardDetailById(@PathVariable Long id) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, boardService.getBoard(id));
    }

    @GetMapping("/all")
    public CommonResponse<?> getBoardAll() {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, boardService.getAllBoards());
    }

}
