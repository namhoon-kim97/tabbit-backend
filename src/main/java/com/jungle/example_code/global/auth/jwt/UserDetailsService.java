package com.jungle.example_code.global.auth.jwt;


import com.jungle.example_code.domain.member.entity.Member;
import com.jungle.example_code.domain.member.repository.MemberRepository;
import com.jungle.example_code.global.exception.NotFoundException;
import com.jungle.example_code.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsernameAndDelYn(username, "N")
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        return new CustomUserDetails(member);
    }
}
