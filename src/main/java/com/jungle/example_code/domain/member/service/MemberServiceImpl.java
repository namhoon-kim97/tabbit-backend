package com.jungle.example_code.domain.member.service;


import com.jungle.example_code.domain.member.dto.MemberRequestDto;
import com.jungle.example_code.domain.member.dto.MemberResponseDto;
import com.jungle.example_code.domain.member.entity.Member;
import com.jungle.example_code.domain.member.repository.MemberRepository;
import com.jungle.example_code.global.auth.jwt.JwtProvider;
import com.jungle.example_code.global.exception.DuplicatedException;
import com.jungle.example_code.global.exception.InvalidRequestException;
import com.jungle.example_code.global.exception.NotFoundException;
import com.jungle.example_code.global.exception.WrongPasswordException;
import com.jungle.example_code.global.model.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.jungle.example_code.global.common.Role.ROLE_USER;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static final String USERNAME_PATTERN = "^[a-z0-9]{4,10}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$";


    @Override
    public void createMember(MemberRequestDto.M_CREATE create) {
        Optional<Member> member = memberRepository.findByEmailAndUsername(create.getEmail(), create.getUsername());
        if (member.isPresent())
            throw new NotFoundException(ResponseStatus.FAIL_MEMBER_DUPLICATED);

        if (!create.getUsername().matches(USERNAME_PATTERN)) {
            throw new InvalidRequestException(ResponseStatus.FAIL_MEMBER_USERNAME_INVALID);
        }
        if (!create.getPassword().matches(PASSWORD_PATTERN)) {
            throw new InvalidRequestException(ResponseStatus.FAIL_MEMBER_PASSWORD_INVALID);
        }
        memberRepository.save(new Member(create.getEmail(), bCryptPasswordEncoder.encode(create.getPassword())
                , create.getUsername(), ROLE_USER, "N"));
    }

    @Override
    public void updateMemberName(Long id, MemberRequestDto.M_UPDATE update) {
        Member member = memberRepository.findMemberByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        if (memberRepository.findByUsernameAndDelYn(update.getUsername(), "N").isPresent())
            throw new DuplicatedException(ResponseStatus.FAIL_MEMBER_USERNAME_DUPLICATED);

        member.updateName(update);
    }

    @Override
    public void updateMemberPassword(Long id, MemberRequestDto.M_UPDATE update) {
        Member member = memberRepository.findMemberByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        member.updatePassword(update);
    }

    @Override
    public void updateMemberRole(Long id, MemberRequestDto.M_UPDATE update) {
        Member member = memberRepository.findMemberByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        member.updateRole(update);
    }

    @Override
    public void deleteMember(Long id, MemberRequestDto.M_DELETE delete) {
        Member member = memberRepository.findMemberByIdAndDelYn(id, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(delete.getPassword(), member.getPassword()))
            throw new WrongPasswordException(ResponseStatus.FAIL_MEMBER_PASSWORD_NOT_MATCHED);

        member.delete();
    }

    @Override
    public MemberResponseDto.READ getMember(MemberRequestDto.M_FIND find) {
        Member member = memberRepository.findByEmailAndDelYn(find.getEmail(), "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        return MemberResponseDto.READ.of(member);
    }

    @Override
    public List<MemberResponseDto.READ> getAllMembers() {
        return memberRepository.findAllByDelYn("N").stream()
                .map(MemberResponseDto.READ::of)
                .collect(Collectors.toList());
    }

    @Override
    public MemberResponseDto.READ login(MemberRequestDto.LOGIN login, HttpServletResponse response) {
        Member member = memberRepository.findMemberByEmailAndDelYn(login.getEmail(), "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        if (!bCryptPasswordEncoder.matches(login.getPassword(), member.getPassword()))
            throw new WrongPasswordException(ResponseStatus.FAIL_MEMBER_PASSWORD_NOT_MATCHED);

        String token = JwtProvider.generateToken(member.getEmail(), member.getRole());

        response.addHeader("Authorization", "Bearer " + token);

        return MemberResponseDto.READ.of(member);
    }

    @Override
    public MemberResponseDto.READ adminGetMember(Long userId) {
        return null;
    }

    @Override
    public List<MemberResponseDto.adminREAD> adminGetMembers(MemberRequestDto.M_CONDITION userRequestDto) {
        return null;
    }

}
