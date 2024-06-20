package com.jungle.Tabbit.domain.waiting.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface WaitingRepository extends Repository<Waiting, Long> {
    void save(Waiting waiting);

    List<Waiting> findByRestaurantAndStatusOrderByQueueNumberAsc(Restaurant restaurant, WaitingStatus status);
}
