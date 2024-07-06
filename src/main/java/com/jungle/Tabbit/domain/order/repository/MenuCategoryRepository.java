package com.jungle.Tabbit.domain.order.repository;

import com.jungle.Tabbit.domain.order.entity.MenuCategory;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface MenuCategoryRepository extends Repository<MenuCategory, Long> {
    List<MenuCategory> findAllByRestaurant_RestaurantId(Long restaurantId);

    void save(MenuCategory menuCategory);

    Optional<MenuCategory> findByCategoryId(Long categoryId);
}
