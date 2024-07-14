package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Guestbook;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GuestbookRepository extends Repository<Guestbook, Long> {
    void save(Guestbook Guestbook);

    Optional<Guestbook> findByGuestbookId(Long id);

    Optional<Guestbook> findByRestaurantAndMappingId(Restaurant restaurant, Long mappingId);

    void delete(Guestbook guestbook);
}
