package com.jungle.Tabbit.domain.waiting.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface WaitingRepository extends Repository<Waiting, Long> {
    void save(Waiting waiting);

    Optional<Waiting> findByWaitingId(Long id);

    @Cacheable("waitingList")
    List<Waiting> findByRestaurantAndWaitingStatusOrderByWaitingNumberAsc(Restaurant restaurant, WaitingStatus status);

    boolean existsByMemberAndRestaurantAndWaitingStatusIn(Member member, Restaurant restaurant, List<WaitingStatus> statuses);

    Long countByRestaurantAndWaitingStatus(Restaurant restaurant, WaitingStatus waitingStatus);

    List<Waiting> findByRestaurantAndWaitingStatus(Restaurant restaurant, WaitingStatus waitingStatus);

    Optional<Waiting> findByRestaurantAndWaitingNumberAndWaitingStatus(Restaurant restaurant, int waitingNumber, WaitingStatus waitingStatus);

    List<Waiting> findByMemberAndWaitingStatusIn(Member member, List<WaitingStatus> waitingStatuses);

    List<Waiting> findByRestaurantAndWaitingStatusInOrderByWaitingNumberAsc(Restaurant restaurant, List<WaitingStatus> waitingStatuses);

    boolean existsByMemberAndWaitingStatusIn(Member member, List<WaitingStatus> waitingStatus);

    List<Waiting> findAllByWaitingStatus(WaitingStatus status);
}