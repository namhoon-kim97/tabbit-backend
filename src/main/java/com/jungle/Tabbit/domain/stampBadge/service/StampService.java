package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import com.jungle.Tabbit.domain.stampBadge.dto.StampResponseDto;
import com.jungle.Tabbit.domain.stampBadge.dto.StampResponseListDto;
import com.jungle.Tabbit.domain.stampBadge.repository.StampRepository;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class StampService {

    private final MemberRepository memberRepository;
    private final StampRepository stampRepository;

    @Transactional(readOnly = true)
    public StampResponseListDto getStampAll(String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        Long totalCount = 0L;
        Long earnedCount = 0L;

        // 시도별 레스토랑 수와 스탬프를 받은 레스토랑 수 조회
        List<Object[]> restaurantCounts = stampRepository.findSidoRestaurantCount(member.getMemberId());

        // 시도별 레스토랑 정보 및 스탬프 여부 조회
        List<Object[]> restaurantList = stampRepository.findRestaurantList(member.getMemberId());

        // 시도별 레스토랑 정보를 맵에 저장
        Map<String, List<RestaurantResponseDto>> restaurantMap = new HashMap<>();
        for (Object[] row : restaurantList) {
            String sidoName = (String) row[0];
            Long restaurantId = (Long) row[1];
            String restaurantName = (String) row[2];
            Boolean hasStamp = (Boolean) row[3];

            RestaurantResponseDto restaurantDto = RestaurantResponseDto.ofRestaurantId(restaurantId, restaurantName, hasStamp);
            restaurantMap.computeIfAbsent(sidoName, k -> new ArrayList<>()).add(restaurantDto);
        }

        // 시도별 레스토랑 수와 스탬프 수를 기반으로 최종 리스트 생성
        List<StampResponseDto> stampResponseDtos = new ArrayList<>();
        for (Object[] row : restaurantCounts) {
            String sidoName = (String) row[0];
            Long totalStampCount = (Long) row[1];
            Long earnedStampCount = (Long) row[2];

            totalCount += totalStampCount;
            earnedCount += earnedStampCount;

            List<RestaurantResponseDto> restaurantDtoList = restaurantMap.getOrDefault(sidoName, new ArrayList<>());
            StampResponseDto stampDto = StampResponseDto.of(sidoName, totalStampCount, earnedStampCount, restaurantDtoList);
            stampResponseDtos.add(stampDto);
        }

        return StampResponseListDto.of(totalCount, earnedCount, stampResponseDtos);
    }
}
