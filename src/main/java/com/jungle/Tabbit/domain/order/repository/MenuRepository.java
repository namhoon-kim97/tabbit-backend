package com.jungle.Tabbit.domain.order.repository;

import com.jungle.Tabbit.domain.order.entity.Menu;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MenuRepository extends Repository<Menu, Long> {
    Optional<Menu> findByMenuId(Long menuId);
}
