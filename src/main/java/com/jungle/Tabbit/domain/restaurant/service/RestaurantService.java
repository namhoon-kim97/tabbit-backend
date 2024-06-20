package com.jungle.Tabbit.domain.restaurant.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantCreateRequestDto;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import com.jungle.Tabbit.domain.restaurant.entity.Address;
import com.jungle.Tabbit.domain.restaurant.entity.Category;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.entity.RestaurantDetail;
import com.jungle.Tabbit.domain.restaurant.repository.AddressRepository;
import com.jungle.Tabbit.domain.restaurant.repository.CategoryRepository;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantDetailRepository;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.global.exception.InvalidRequestException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import com.jungle.Tabbit.global.util.SecurityUtil;
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
    private final RestaurantDetailRepository restaurantDetailRepository;
    private final MemberRepository memberRepository;
    private final AddressRepository addressRepository;
    private final CategoryRepository categoryRepository;

    public List<RestaurantResponseDto> getAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(restaurant -> RestaurantResponseDto.of(restaurant, false))
                .collect(Collectors.toList());
    }

    public void createRestaurant(RestaurantCreateRequestDto restaurantCreateRequestDto) {
        Member owner = memberRepository.findMemberByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        Category category = categoryRepository.findByCategoryCd(restaurantCreateRequestDto.getCategoryCd())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CATEGORY_NOT_FOUND));

        String[] parts = restaurantCreateRequestDto.getAddress_name().split(" ");
        if (parts.length < 3) {
            throw new InvalidRequestException(ResponseStatus.FAIL_ADDRESS_NOT_SUCCESS);
        }
        Address address = new Address(
                parts[0],
                parts[1],
                parts[2],
                restaurantCreateRequestDto.getRoad_address_name(),
                restaurantCreateRequestDto.getAddress_name(),
                restaurantCreateRequestDto.getDetail_address()
        );
//        address = addressRepository.save(address);

        RestaurantDetail restaurantDetail = new RestaurantDetail(
                restaurantCreateRequestDto.getOpening_hours(),
                restaurantCreateRequestDto.getBreak_time(),
                restaurantCreateRequestDto.getHolidays(),
                restaurantCreateRequestDto.getRestaurant_number(),
                restaurantCreateRequestDto.getDescription()
        );
//        restaurantDetail = restaurantDetailRepository.save(restaurantDetail);

        Restaurant restaurant = new Restaurant(
                restaurantDetail,
                owner,
                restaurantCreateRequestDto.getPlace_name(),
                category,
                address,
                restaurantCreateRequestDto.getLatitude(),
                restaurantCreateRequestDto.getLongitude(),
                restaurantCreateRequestDto.getEstimatedTimePerTeam()
        );
        restaurantRepository.save(restaurant);

//        return RestaurantResponseDto.of(restaurant, false);
    }
}
