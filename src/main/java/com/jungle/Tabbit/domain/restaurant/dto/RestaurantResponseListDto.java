package com.jungle.Tabbit.domain.restaurant.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RestaurantResponseListDto {
    private List<RestaurantResponseDto> restaurantResponseList;
}
