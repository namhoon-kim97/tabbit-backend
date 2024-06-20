package com.jungle.Tabbit.domain.member.controller;

import com.jungle.Tabbit.domain.member.dto.MemberJoinRequestDto;
import com.jungle.Tabbit.domain.member.dto.MemberLoginRequestDto;
import com.jungle.Tabbit.domain.member.service.MemberService;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public CommonResponse<?> join(@RequestBody @Valid MemberJoinRequestDto memberJoinRequestDto) {
        memberService.join(memberJoinRequestDto);

        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto) {
        String token = memberService.login(memberLoginRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);

        CommonResponse<Object> resp = CommonResponse.success(ResponseStatus.SUCCESS_CREATE);

        return new ResponseEntity<>(resp, headers, HttpStatus.OK);
    }
}
