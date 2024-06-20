package com.jungle.Tabbit.domain.restaurant.repository;

import com.jungle.Tabbit.domain.restaurant.entity.RestaurantDetail;
import org.springframework.data.repository.Repository;

public interface RestaurantDetailRepository extends Repository<RestaurantDetail, Long> {
    RestaurantDetail save(RestaurantDetail restaurantDetail);
}
