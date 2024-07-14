package com.jungle.Tabbit.domain.stampBadge.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.stampBadge.dto.SidoStampResponseDto;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StampRepository extends Repository<MemberStamp, Long> {
    void save(MemberStamp memberStamp);

    Optional<MemberStamp> findByMemberAndRestaurant(Member member, Restaurant restaurant);

    @Query("SELECT new com.jungle.Tabbit.domain.stampBadge.dto.SidoStampResponseDto(a.sido, COUNT(r.restaurantId), COUNT(ms.restaurant))" +
            "FROM address a " +
            "LEFT JOIN restaurant r ON a.addressId = r.address.addressId " +
            "LEFT JOIN memberStamp ms ON r.restaurantId = ms.restaurant.restaurantId AND ms.member.memberId = :memberId " +
            "GROUP BY a.sido")
    List<SidoStampResponseDto> findSidoStampCount(@Param("memberId") Long memberId);

    @Query("SELECT a.sido, a.sigungu, r.restaurantId, r.name AS restaurantName, " +
            "CASE WHEN ms.member.memberId IS NOT NULL THEN true ELSE false END AS hasStamp " +
            "FROM address a " +
            "INNER JOIN restaurant r ON a.addressId = r.address.addressId " +
            "LEFT JOIN memberStamp ms ON r.restaurantId = ms.restaurant.restaurantId AND ms.member.memberId = :memberId ")
    List<Object[]> findRestaurantList(@Param("memberId") Long memberId);

    List<MemberStamp> findByMember(Member member);
}
