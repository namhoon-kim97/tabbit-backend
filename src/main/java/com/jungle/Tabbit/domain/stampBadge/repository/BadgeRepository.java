package com.jungle.Tabbit.domain.stampBadge.repository;

import com.jungle.Tabbit.domain.stampBadge.entity.Badge;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BadgeRepository extends Repository<Badge, Long> {
    List<Badge> findAll();
}
