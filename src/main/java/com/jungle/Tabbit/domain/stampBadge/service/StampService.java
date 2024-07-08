package com.jungle.Tabbit.domain.stampBadge.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseDto;
import com.jungle.Tabbit.domain.stampBadge.dto.SidoStamp;
import com.jungle.Tabbit.domain.stampBadge.dto.SigunguStamp;
import com.jungle.Tabbit.domain.stampBadge.dto.StampListDto;
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
import java.util.stream.Collectors;

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

        // 시도별 총 스탬프 수와 획득 스탬프 수 조회
        List<SidoStamp> sidoStampList = stampRepository.findSidoStampCount(member.getMemberId());
        // 시도/시군구별 레스토랑 정보 및 스탬프 여부 조회
        List<Object[]> sidoRestaurantList = stampRepository.findRestaurantList(member.getMemberId());

        long totalCount = sidoStampList.stream().mapToLong(SidoStamp::getTotalsidoStampCount).sum();
        long earnedCount = sidoStampList.stream().mapToLong(SidoStamp::getEarnedsidoStampCount).sum();

        // 레스토랑 정보를 시도/시군구별 맵에 저장
        Map<String, Map<String, List<RestaurantResponseDto>>> restaurantMap = new HashMap<>();
        for (Object[] row : sidoRestaurantList) {
            String sidoName = (String) row[0];
            String sigunguName = (String) row[1];
            Long restaurantId = (Long) row[2];
            String restaurantName = (String) row[3];
            Boolean hasStamp = (Boolean) row[4];

            RestaurantResponseDto restaurantDto = RestaurantResponseDto.ofRestaurantId(restaurantId, restaurantName, hasStamp);
            restaurantMap.computeIfAbsent(sidoName, k -> new HashMap<>())
                    .computeIfAbsent(sigunguName, k -> new ArrayList<>())
                    .add(restaurantDto);
        }

        // 시도별로 시군구와 레스토랑 리스트를 포함한 객체를 생성
        List<StampListDto> stampList = new ArrayList<>();
        for (Map.Entry<String, Map<String, List<RestaurantResponseDto>>> sidoEntry : restaurantMap.entrySet()) {
            String sidoName = sidoEntry.getKey();
            Map<String, List<RestaurantResponseDto>> sigunguMap = sidoEntry.getValue();

            List<SigunguStamp> sigunguStampList = sigunguMap.entrySet().stream()
                    .map(sigunguEntry -> {
                        String sigunguName = sigunguEntry.getKey();
                        List<RestaurantResponseDto> restaurantList = sigunguEntry.getValue();

                        long totalSigunguStampCount = restaurantList.size();
                        long earnedSigunguStampCount = restaurantList.stream().filter(RestaurantResponseDto::getHasStamp).count();

                        return SigunguStamp.of(sigunguName, totalSigunguStampCount, earnedSigunguStampCount, restaurantList);
                    })
                    .collect(Collectors.toList());

            StampListDto stamp = StampListDto.of(sidoName, sigunguStampList);
            stampList.add(stamp);
        }

        return StampResponseListDto.of(totalCount, earnedCount, sidoStampList, stampList);
    }
}
