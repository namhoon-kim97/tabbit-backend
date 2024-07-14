package com.jungle.Tabbit.domain.member.controller;

import com.jungle.Tabbit.domain.member.dto.*;
import com.jungle.Tabbit.domain.member.service.MemberService;
import com.jungle.Tabbit.global.config.security.CustomUserDetails;
import com.jungle.Tabbit.global.model.CommonResponse;
import com.jungle.Tabbit.global.model.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "회원을 등록합니다.")
    @ApiResponse(responseCode = "200", description = "회원 가입 성공")
    public CommonResponse<?> join(@RequestBody @Valid MemberJoinRequestDto memberJoinRequestDto) {
        memberService.join(memberJoinRequestDto);

        return CommonResponse.success(ResponseStatus.SUCCESS_CREATE);
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "로그인 후 기기의 fcm 토큰을 업데이트 합니다.")
    @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(schema = @Schema(implementation = MemberLoginResponseDto.class)))
    public CommonResponse<?> login(@RequestBody MemberLoginRequestDto memberLoginRequestDto, HttpServletResponse response) {
        MemberLoginDto memberLoginDto = memberService.login(memberLoginRequestDto);
        response.setHeader("Authorization", memberLoginDto.getToken());

        MemberLoginResponseDto memberLoginResponseDto = memberLoginDto.toResponseDto();

        return CommonResponse.success(ResponseStatus.SUCCESS_LOGIN, memberLoginResponseDto);
    }

    @PutMapping("/info")
    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "회원 정보 수정 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> update(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid MemberUpdateRequestDto requestDto) {
        memberService.updateMemberInfo(userDetails.getUsername(), requestDto);

        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }

    @PutMapping("/password")
    public CommonResponse<?> updatePassword(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody @Valid MemberPasswordUpdateDto memberPasswordUpdateDto) {
        memberService.updateMemberPassword(userDetails.getUsername(), memberPasswordUpdateDto);

        return CommonResponse.success(ResponseStatus.SUCCESS_UPDATE);
    }
    //회원탈퇴
    @DeleteMapping("/delete")
    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴합니다.")
    @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    public CommonResponse<?> delete(@AuthenticationPrincipal CustomUserDetails userDetails,@RequestBody @Valid MemberDeleteRequestDto memberDeleteRequestDto) {
        memberService.deleteMember(userDetails.getUsername(), memberDeleteRequestDto);

        return CommonResponse.success(ResponseStatus.SUCCESS_DELETE);
    }
}
