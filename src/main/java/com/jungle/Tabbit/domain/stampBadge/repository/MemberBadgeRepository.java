package com.jungle.Tabbit.domain.stampBadge.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberBadge;
import org.springframework.data.repository.Repository;

public interface MemberBadgeRepository extends Repository<MemberBadge, Long> {

    void save(MemberBadge memberBadge);

    boolean existsByMemberAndBadge(Member member, Badge badge);
}
