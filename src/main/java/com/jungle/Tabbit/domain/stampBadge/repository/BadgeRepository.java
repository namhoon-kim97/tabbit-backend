package com.jungle.Tabbit.domain.stampBadge.repository;

import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends Repository<Badge, Long> {
    List<Badge> findAll();

    Optional<Badge> findByBadgeId(Long id);
}
