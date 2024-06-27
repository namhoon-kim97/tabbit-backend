package com.jungle.Tabbit.domain.member.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);
    Optional<Member> findMemberByUsername(String username);
    Optional<Member> findMemberByMemberId(Long memberId);

}
