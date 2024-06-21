package com.jungle.Tabbit.domain.restaurant.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.entity.MemberRole;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantCreateRequestDto;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import com.jungle.Tabbit.domain.restaurant.entity.Address;
import com.jungle.Tabbit.domain.restaurant.entity.Category;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.entity.RestaurantDetail;
import com.jungle.Tabbit.domain.restaurant.repository.CategoryRepository;
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
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;

    public List<RestaurantResponseDto> getAllRestaurant() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        return restaurants.stream()
                .map(restaurant -> RestaurantResponseDto.of(restaurant, false))
                .collect(Collectors.toList());
    }

    public void createRestaurant(RestaurantCreateRequestDto requestDto) {
        Member owner = memberRepository.findMemberByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        if (!owner.getMemberRole().equals(MemberRole.ROLE_MANAGER)) {
            throw new InvalidRequestException(ResponseStatus.FAIL_MEMBER_ROLE_INVALID);
        }

        Category category = categoryRepository.findByCategoryCd(requestDto.getCategoryCd())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CATEGORY_NOT_FOUND));

        String[] parts = requestDto.getAddress_name().split(" ");
        if (parts.length < 3) {
            throw new InvalidRequestException(ResponseStatus.FAIL_ADDRESS_NOT_SUCCESS);
        }

        Address address = new Address(
                parts[0],
                parts[1],
                parts[2],
                requestDto.getRoad_address_name(),
                requestDto.getAddress_name(),
                requestDto.getDetail_address()
        );

        RestaurantDetail restaurantDetail = new RestaurantDetail(
                requestDto.getOpening_hours(),
                requestDto.getBreak_time(),
                requestDto.getHolidays(),
                requestDto.getRestaurant_number(),
                requestDto.getDescription()
        );

        Restaurant restaurant = new Restaurant(
                restaurantDetail,
                owner,
                requestDto.getPlace_name(),
                category,
                address,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getEstimatedTimePerTeam()
        );
        restaurantRepository.save(restaurant);
    }
}
