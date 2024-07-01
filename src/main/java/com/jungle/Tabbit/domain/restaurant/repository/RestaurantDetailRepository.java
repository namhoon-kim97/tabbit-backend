package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.entity.RestaurantDetail;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface RestaurantDetailRepository extends Repository<RestaurantDetail, Long> {
    void save(RestaurantDetail restaurantDetail);

    Optional<RestaurantDetail> findByRestaurant(Restaurant restaurant);
}
