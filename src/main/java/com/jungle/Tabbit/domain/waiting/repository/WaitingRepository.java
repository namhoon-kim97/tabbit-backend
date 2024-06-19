package com.jungle.Tabbit.domain.waiting.repository;

import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import org.springframework.data.repository.Repository;

public interface WaitingRepository extends Repository<Waiting, Long> {
    void save(Waiting waiting);
}
