package com.jungle.example_code.domain.reply.controller;


import com.jungle.example_code.domain.reply.dto.ReplyRequestDto;
import com.jungle.example_code.domain.reply.service.ReplyService;
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
@RequestMapping("/api/v1/replys")
public class ReplyController {

    private final ReplyService replyService;

    @PostMapping("/create")
    public CommonResponse<?> createReply(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody ReplyRequestDto.R_CREATE create) {
        replyService.createReply(userDetails.getUsername(), create);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @PutMapping("/update/{id}")
    public CommonResponse<?> updateReply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id, @RequestBody ReplyRequestDto.R_UPDATE update) {
        replyService.updateReply(userDetails.getUsername(), userDetails.getRole(), id, update);
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<?> deleteReply(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long id, @RequestBody ReplyRequestDto.R_DELETE delete) {
        replyService.deleteReply(userDetails.getRole(), id, delete);
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }
}
