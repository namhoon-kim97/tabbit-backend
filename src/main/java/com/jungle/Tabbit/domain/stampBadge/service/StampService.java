package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.stampBadge.dto.StampResponseDto;
import com.jungle.Tabbit.domain.stampBadge.dto.StampResponseListDto;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StampService {

    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional(readOnly = true)
    public StampResponseListDto getStampAll(String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        List<StampResponseDto> result = new ArrayList<>();
        List<String> sidos = getSidoList();

        Set<Long> stampedRestaurantIds = member.getMemberStampList().stream()
                .map(stamp -> stamp.getRestaurant().getRestaurantId())
                .collect(Collectors.toSet());

        // 시/도별로 레스토랑을 그룹화
        Map<String, List<Restaurant>> restaurantsBySido = restaurantRepository.findAllWithAddress().stream()
                .collect(Collectors.groupingBy(restaurant -> restaurant.getAddress().getSido()));

        for (String sido : sidos) {
            List<RestaurantResponseDto> restaurantResponseList = restaurantsBySido.getOrDefault(sido, Collections.emptyList()).stream()
                    .map(restaurant -> RestaurantResponseDto.of(restaurant, stampedRestaurantIds.contains(restaurant.getRestaurantId())))
                    .collect(Collectors.toList());

            long earnedCount = restaurantResponseList.stream()
                    .filter(RestaurantResponseDto::getEarnedStamp)
                    .count();

            result.add(StampResponseDto.of(sido, (long) restaurantResponseList.size(), earnedCount, restaurantResponseList));
        }

        return StampResponseListDto.builder().stampResponseList(result).build();
    }

    private List<String> getSidoList() {
        return Arrays.asList("서울시", "부산시", "대구시", "인천시", "광주시", "대전시", "울산시", "세종시", "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도", "제주도");
    }
}
