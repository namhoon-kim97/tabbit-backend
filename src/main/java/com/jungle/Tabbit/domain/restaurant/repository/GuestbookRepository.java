package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Guestbook;
import org.springframework.data.repository.Repository;

public interface GuestbookRepository extends Repository<Guestbook, Long> {
    void save(Guestbook Guestbook);
}
