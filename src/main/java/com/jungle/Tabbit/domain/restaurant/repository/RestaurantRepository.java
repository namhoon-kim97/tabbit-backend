package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface RestaurantRepository extends Repository<Restaurant, Long> {
    List<Restaurant> findAll();

    Restaurant save(Restaurant restaurant);

    Restaurant findByRestaurantId(Long id);
}
