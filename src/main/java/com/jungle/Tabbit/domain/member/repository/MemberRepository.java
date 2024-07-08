package com.jungle.Tabbit.domain.member.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, Long> {
    Member save(Member member);
    @Query("SELECT m FROM member m WHERE m.username = :username AND m.isDeleted = false")
    Optional<Member> findMemberByUsername(@Param("username") String username);
    @Query("SELECT m FROM member m WHERE m.memberId = :memberId AND m.isDeleted = false")
    Optional<Member> findMemberByMemberId(@Param("memberId") Long memberId);

}
