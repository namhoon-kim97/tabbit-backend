package com.jungle.Tabbit.domain.restaurant.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.entity.MemberRole;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.restaurant.dto.*;
import com.jungle.Tabbit.domain.restaurant.entity.Address;
import com.jungle.Tabbit.domain.restaurant.entity.Category;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.entity.RestaurantDetail;
import com.jungle.Tabbit.domain.restaurant.repository.CategoryRepository;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.stampBadge.repository.StampRepository;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import com.jungle.Tabbit.domain.waiting.repository.WaitingRepository;
import com.jungle.Tabbit.global.exception.InvalidRequestException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MemberRepository memberRepository;
    private final StampRepository stampRepository;
    private final CategoryRepository categoryRepository;
    private final WaitingRepository waitingRepository;

    @Transactional(readOnly = true)
    public RestaurantResponseListDto getAllRestaurant(String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        List<Restaurant> restaurantList = restaurantRepository.findAll();

        Set<Long> stampedRestaurantIds = member.getMemberStampList().stream()
                .map(stamp -> stamp.getRestaurant().getRestaurantId())
                .collect(Collectors.toSet());

        List<RestaurantResponseDto> restaurantResponseList = restaurantList.stream()
                .map(restaurant -> RestaurantResponseDto.of(restaurant, stampedRestaurantIds.contains(restaurant.getRestaurantId())))
                .collect(Collectors.toList());

        return RestaurantResponseListDto.builder().restaurantResponseList(restaurantResponseList).build();
    }

    public void createRestaurant(RestaurantCreateRequestDto requestDto, String username) {
        Member owner = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        if (!owner.getMemberRole().equals(MemberRole.ROLE_MANAGER)) {
            throw new InvalidRequestException(ResponseStatus.FAIL_MEMBER_ROLE_INVALID);
        }

        Category category = categoryRepository.findByCategoryCd(requestDto.getCategoryCd())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_CATEGORY_NOT_FOUND));

        Address address = new Address(
                requestDto.getSido(),
                requestDto.getSigungu(),
                requestDto.getEupmyeondong(),
                requestDto.getRoadAddressName(),
                requestDto.getAddressName(),
                requestDto.getDetailAddress()
        );

        RestaurantDetail restaurantDetail = new RestaurantDetail(
                requestDto.getOpeningHours(),
                requestDto.getBreakTime(),
                requestDto.getHolidays(),
                requestDto.getRestaurantNumber(),
                requestDto.getDescription()
        );

        Restaurant restaurant = new Restaurant(
                restaurantDetail,
                owner,
                requestDto.getPlaceName(),
                category,
                address,
                requestDto.getLatitude(),
                requestDto.getLongitude(),
                requestDto.getEstimatedTimePerTeam()
        );
        restaurantRepository.save(restaurant);
    }

    @Transactional(readOnly = true)
    public RestaurantResponseSummaryDto getRestaurantSummaryInfo(Long restaurantId, String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));

        Boolean earnedStamp = stampRepository.findByMemberAndRestaurant(member, restaurant).isPresent();

        Long currentWaitingNumber = waitingRepository.countByRestaurantAndWaitingStatus(restaurant, WaitingStatus.STATUS_WAITING);

        return RestaurantResponseSummaryDto.of(restaurant, earnedStamp, currentWaitingNumber);
    }

    @Transactional(readOnly = true)
    public RestaurantResponseDetailDto getRestaurantDetailInfo(Long restaurantId, String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));

        Boolean earnedStamp = stampRepository.findByMemberAndRestaurant(member, restaurant).isPresent();

        return RestaurantResponseDetailDto.of(restaurant, earnedStamp);
    }
}
