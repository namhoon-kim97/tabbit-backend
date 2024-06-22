package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends Repository<Restaurant, Long> {
    List<Restaurant> findAll();

    void save(Restaurant restaurant);

    Optional<Restaurant> findByRestaurantId(Long id);
}
