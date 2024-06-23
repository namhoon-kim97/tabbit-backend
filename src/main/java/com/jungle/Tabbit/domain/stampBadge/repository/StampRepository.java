package com.jungle.Tabbit.domain.stampBadge.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface StampRepository extends Repository<MemberStamp, Long> {
    void save(MemberStamp memberStamp);

    Optional<MemberStamp> findByMemberAndRestaurant(Member member, Restaurant restaurant);
}
