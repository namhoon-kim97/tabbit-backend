package com.jungle.Tabbit.domain.member.service;

import com.jungle.Tabbit.domain.member.dto.MemberJoinRequestDto;
import com.jungle.Tabbit.domain.member.dto.MemberLoginRequestDto;
import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberBadge;
import com.jungle.Tabbit.domain.stampBadge.repository.BadgeRepository;
import com.jungle.Tabbit.domain.stampBadge.repository.MemberBadgeRepository;
import com.jungle.Tabbit.global.config.security.jwt.JwtProvider;
import com.jungle.Tabbit.global.exception.DuplicatedException;
import com.jungle.Tabbit.global.exception.LoginFailException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.jungle.Tabbit.global.model.ResponseStatus.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final BadgeRepository badgeRepository;
    private final MemberBadgeRepository memberBadgeRepository;


    public void join(MemberJoinRequestDto memberJoinRequestDto) {
        Optional<Member> findMember = memberRepository.findMemberByUsername(memberJoinRequestDto.getUsername());

        if (findMember.isPresent()) {
            throw new DuplicatedException(FAIL_MEMBER_DUPLICATED);
        }

        Badge defaultBadge = badgeRepository.findByBadgeId(1L)
                .orElseThrow(() -> new NotFoundException(FAIL_BADGE_NOT_FOUND));

        Member createMember = memberJoinRequestDto.createMember(passwordEncoder, defaultBadge);

        memberRepository.save(createMember);
        memberBadgeRepository.save(new MemberBadge(createMember, defaultBadge));
    }


    public String login(MemberLoginRequestDto memberLoginRequestDto) {
        Authentication authenticate = null;
        try {
            authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            memberLoginRequestDto.getUsername(),
                            memberLoginRequestDto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new LoginFailException(FAIL_LOGIN_NOT_SUCCESS);
        }

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        // FCM 토큰 업데이트
        Member member = memberRepository.findMemberByUsername(memberLoginRequestDto.getUsername())
                .orElseThrow(() -> new LoginFailException(FAIL_LOGIN_NOT_SUCCESS));
        member.updateFcmToken(memberLoginRequestDto.getFcmToken());

        return jwtProvider.generateToken(authenticate.getName());
    }
}
