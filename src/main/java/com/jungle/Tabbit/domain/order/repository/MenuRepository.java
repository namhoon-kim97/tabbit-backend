package com.jungle.Tabbit.domain.order.repository;

import com.jungle.Tabbit.domain.order.entity.Menu;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MenuRepository extends Repository<Menu, Long> {
    Optional<Menu> findByMenuId(Long menuId);

    @EntityGraph(attributePaths = {"category"})
    List<Menu> findAllByRestaurant(Restaurant restaurant);

    void save(Menu menu);
}
