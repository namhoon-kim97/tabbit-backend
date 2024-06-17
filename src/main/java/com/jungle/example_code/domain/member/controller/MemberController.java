package com.jungle.example_code.domain.member.controller;

import com.jungle.example_code.domain.member.dto.MemberRequestDto;
import com.jungle.example_code.domain.member.service.MemberService;
import com.jungle.example_code.global.model.CommonResponse;
import com.jungle.example_code.global.model.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberController {


    private final MemberService memberService;

    @PostMapping("/join")
    public CommonResponse<?> createMember(@RequestBody MemberRequestDto.M_CREATE create) {
        memberService.createMember(create);
        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @PostMapping("/login")
    public CommonResponse<?> login(@RequestBody MemberRequestDto.LOGIN login, HttpServletResponse response) {
        return CommonResponse.success(ResponseStatus.SUCCESS_LOGIN, memberService.login(login, response));
    }

    @PutMapping("/update/name/{id}")
    public CommonResponse<String> updateMemberName(@PathVariable Long id, @RequestBody MemberRequestDto.M_UPDATE update) {
        memberService.updateMemberName(id, update);
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/update/password/{id}")
    public CommonResponse<String> updateMemberPassword(@PathVariable Long id, @RequestBody MemberRequestDto.M_UPDATE update) {
        memberService.updateMemberPassword(id, update);
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/update/role/{id}")
    public CommonResponse<String> updateMemberRole(@PathVariable Long id, @RequestBody MemberRequestDto.M_UPDATE update) {
        memberService.updateMemberRole(id, update);
        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse<String> deleteMember(@PathVariable Long id, @RequestBody MemberRequestDto.M_DELETE delete) {
        memberService.deleteMember(id, delete);
        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }

    @GetMapping("/{email}")
    public CommonResponse<?> getMemberDetailByEmail(@PathVariable String email) {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, memberService.getMember(new MemberRequestDto.M_FIND(email)));
    }

    @GetMapping("/all")
    public CommonResponse<?> getMemberAll() {
        return CommonResponse.success(ResponseStatus.SUCCESS_OK, memberService.getAllMembers());
    }

}
