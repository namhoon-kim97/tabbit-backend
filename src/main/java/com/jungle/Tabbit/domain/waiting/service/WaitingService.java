package com.jungle.Tabbit.domain.waiting.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.nfc.entity.Nfc;
import com.jungle.Tabbit.domain.nfc.repository.NfcRepository;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.waiting.dto.WaitingListResponseDto;
import com.jungle.Tabbit.domain.waiting.dto.WaitingRequestCreateDto;
import com.jungle.Tabbit.domain.waiting.dto.WaitingResponseDto;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import com.jungle.Tabbit.domain.waiting.repository.WaitingRepository;
import com.jungle.Tabbit.global.exception.BusinessLogicException;
import com.jungle.Tabbit.global.exception.DuplicatedException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final NfcRepository nfcRepository;
    private final MemberRepository memberRepository;
    private static final ConcurrentHashMap<Long, AtomicLong> storeQueueNumbers = new ConcurrentHashMap<>();  // 가게별 전역 대기번호 변수
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public WaitingResponseDto registerWaiting(WaitingRequestCreateDto requestDto, String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        Nfc nfc = nfcRepository.findByNfcId(requestDto.getNfcId())
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_NFC_NOT_FOUND));

        Restaurant restaurant = nfc.getRestaurant();

        // 사용자가 동일한 레스토랑에서 대기 중인지 확인
        boolean alreadyWaiting = waitingRepository.existsByMemberAndRestaurantAndWaitingStatus(member, restaurant, WaitingStatus.STATUS_WAITING);
        if (alreadyWaiting) {
            throw new DuplicatedException(ResponseStatus.FAIL_MEMBER_WAITING_DUPLICATED);
        }

        Long queueNumber = getNextQueueNumber(restaurant.getRestaurantId());

        Waiting waiting = new Waiting(requestDto.getPeopleNumber(), queueNumber.intValue(), restaurant, WaitingStatus.STATUS_WAITING, member);
        waitingRepository.save(waiting);

        int currentWaitingPosition = getCurrentWaitingPosition(waiting);
        Long estimatedWaitTime = calculateEstimatedWaitTime(currentWaitingPosition, restaurant.getEstimatedTimePerTeam());

        return WaitingResponseDto.of(waiting, estimatedWaitTime, currentWaitingPosition);
    }

    @Transactional(readOnly = true)
    public WaitingResponseDto getWaitingOverview(Long restaurantId, String username) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        Waiting userWaiting = waitingRepository.findByRestaurantAndMemberAndWaitingStatus(restaurant, member, WaitingStatus.STATUS_WAITING)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_GET_CURRENT_WAIT_POSITION));

        int currentWaitingPosition = getCurrentWaitingPosition(userWaiting);
        Long estimatedWaitTime = calculateEstimatedWaitTime(currentWaitingPosition, restaurant.getEstimatedTimePerTeam());

        return WaitingResponseDto.of(userWaiting, estimatedWaitTime, currentWaitingPosition);
    }


    @Transactional(readOnly = true)
    public WaitingListResponseDto getUserWaitingList(String username) {
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));

        List<Waiting> waitingList = waitingRepository.findByMemberAndWaitingStatus(member, WaitingStatus.STATUS_WAITING);

        List<WaitingResponseDto> waitingResponseDtos = waitingList.stream()
                .map(waiting -> {
                    int currentWaitingPosition = getCurrentWaitingPosition(waiting);
                    Long estimatedWaitTime = calculateEstimatedWaitTime(currentWaitingPosition, waiting.getRestaurant().getEstimatedTimePerTeam());
                    return WaitingResponseDto.of(waiting, estimatedWaitTime, currentWaitingPosition);
                })
                .collect(Collectors.toList());

        return WaitingListResponseDto.builder()
                .waitingResponseDtos(waitingResponseDtos)
                .build();
    }

    @Transactional
    public void deleteWaiting(Long restaurantId, String username) {
        Restaurant restaurant = restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
        Waiting waiting = waitingRepository.findByRestaurantAndMemberAndWaitingStatus(restaurant, member, WaitingStatus.STATUS_WAITING)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_GET_CURRENT_WAIT_POSITION));

        waiting.updateStatus(WaitingStatus.STATUS_CANCELLED);
        waitingRepository.save(waiting);
    }

    private Long getNextQueueNumber(Long storeId) {
        storeQueueNumbers.putIfAbsent(storeId, new AtomicLong(0));
        return storeQueueNumbers.get(storeId).incrementAndGet();
    }

    private int getCurrentWaitingPosition(Waiting waiting) {
        List<Waiting> waitingList = waitingRepository.findByRestaurantAndWaitingStatusOrderByWaitingNumberAsc(waiting.getRestaurant(), WaitingStatus.STATUS_WAITING);
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getWaitingId().equals(waiting.getWaitingId())) {
                return i;
            }
        }
        throw new BusinessLogicException(ResponseStatus.FAIL_MEMBER_WAITING_DUPLICATED);
    }

    private Long calculateEstimatedWaitTime(int position, Long estimatedTimePerTeam) {
        return position * estimatedTimePerTeam;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetQueueNumbers() {
        storeQueueNumbers.replaceAll((storeId, queueNumber) -> new AtomicLong(0));
    }
}