package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Guestbook;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface GuestbookRepository extends Repository<Guestbook, Long> {
    void save(Guestbook Guestbook);

    Optional<Guestbook> findByGuestbookId(Long id);

    void delete(Guestbook guestbook);
}
