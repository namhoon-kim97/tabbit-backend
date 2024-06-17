package com.jungle.example_code.domain.member.service;


import com.jungle.example_code.domain.member.dto.MemberRequestDto;
import com.jungle.example_code.domain.member.dto.MemberResponseDto;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface MemberService {


    public void createMember(MemberRequestDto.M_CREATE create);

    public void updateMemberName(Long id, MemberRequestDto.M_UPDATE update);

    public void updateMemberPassword(Long id, MemberRequestDto.M_UPDATE update);

    public void updateMemberRole(Long id, MemberRequestDto.M_UPDATE update);

    public void deleteMember(Long id, MemberRequestDto.M_DELETE delete);

    public MemberResponseDto.READ getMember(MemberRequestDto.M_FIND find);

    public List<MemberResponseDto.READ> getAllMembers();

    public MemberResponseDto.READ login(MemberRequestDto.LOGIN userRequestDto, HttpServletResponse response);

    public MemberResponseDto.READ adminGetMember(Long userId);

    public List<MemberResponseDto.adminREAD> adminGetMembers(MemberRequestDto.M_CONDITION userRequestDto);
}
