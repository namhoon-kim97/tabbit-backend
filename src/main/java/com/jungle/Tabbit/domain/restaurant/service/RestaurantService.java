package com.jungle.Tabbit.domain.restaurant.service;

import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<RestaurantResponseDto> getAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(restaurant -> RestaurantResponseDto.of(restaurant, false))
                .collect(Collectors.toList());
    }
}
