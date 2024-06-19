package com.jungle.Tabbit.domain.waiting.service;

import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.waiting.dto.WaitingRequestCreateDto;
import com.jungle.Tabbit.domain.waiting.dto.WaitingResponseDto;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import com.jungle.Tabbit.domain.waiting.repository.WaitingRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final RestaurantRepository restaurantRepository;
    private static final ConcurrentHashMap<Long, AtomicLong> storeQueueNumbers = new ConcurrentHashMap<>();  // 가게별 전역 대기번호 변수

    @Transactional
    public WaitingResponseDto registerWaiting(WaitingRequestCreateDto requestDto, String nfcId) {
        Restaurant restaurant = restaurantRepository.findByNfcId(nfcId);
        Long queueNumber = getNextQueueNumber(restaurant.getId());

        Waiting waiting = new Waiting(requestDto.getPeopleNumber(), queueNumber.intValue(), restaurant.getId(), WaitingStatus.STATUS_WAITING);
        waitingRepository.save(waiting);

        return WaitingResponseDto.of(waiting);
    }

    private Long getNextQueueNumber(Long storeId) {
        storeQueueNumbers.putIfAbsent(storeId, new AtomicLong(0));
        return storeQueueNumbers.get(storeId).incrementAndGet();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetQueueNumbers() {
        storeQueueNumbers.replaceAll((storeId, queueNumber) -> new AtomicLong(0));
    }
}
