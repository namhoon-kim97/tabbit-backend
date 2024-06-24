package com.jungle.Tabbit.domain.waiting.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.nfc.entity.Nfc;
import com.jungle.Tabbit.domain.nfc.repository.NfcRepository;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseSummaryDto;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import com.jungle.Tabbit.domain.stampBadge.repository.StampRepository;
import com.jungle.Tabbit.domain.waiting.dto.*;
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
    private final RestaurantRepository restaurantRepository;
    private final StampRepository stampRepository;
    private static final ConcurrentHashMap<Long, AtomicLong> storeQueueNumbers = new ConcurrentHashMap<>();  // 가게별 전역 대기번호 변수

    @Transactional
    public WaitingResponseDto registerWaiting(WaitingRequestCreateDto requestDto, String username) {
        Member member = getMemberByUsername(username);
        Nfc nfc = getNfcById(requestDto.getNfcId());
        Restaurant restaurant = nfc.getRestaurant();

        validateDuplicateWaiting(member, restaurant);

        Long queueNumber = getNextQueueNumber(restaurant.getRestaurantId());
        Waiting waiting = new Waiting(requestDto.getPeopleNumber(), queueNumber.intValue(), restaurant, WaitingStatus.STATUS_WAITING, member);
        waitingRepository.save(waiting);

        int currentWaitingPosition = getCurrentWaitingPosition(waiting);
        Long estimatedWaitTime = calculateEstimatedWaitTime(currentWaitingPosition, restaurant.getEstimatedTimePerTeam());

        return WaitingResponseDto.of(waiting, estimatedWaitTime, currentWaitingPosition);
    }

    @Transactional(readOnly = true)
    public WaitingResponseDto getWaitingOverview(Long restaurantId, String username) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);
        Waiting userWaiting = getWaitingByMemberAndRestaurant(member, restaurant);

        int currentWaitingPosition = getCurrentWaitingPosition(userWaiting);
        Long estimatedWaitTime = calculateEstimatedWaitTime(currentWaitingPosition, restaurant.getEstimatedTimePerTeam());

        return WaitingResponseDto.of(userWaiting, estimatedWaitTime, currentWaitingPosition);
    }

    @Transactional(readOnly = true)
    public WaitingListResponseDto getUserWaitingList(String username) {
        Member member = getMemberByUsername(username);
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
    public void cancelWaiting(Long restaurantId, String username) {
        Member member = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);
        Waiting waiting = getWaitingByMemberAndRestaurant(member, restaurant);

        waiting.updateStatus(WaitingStatus.STATUS_CANCELLED);
    }

    @Transactional
    public void confirmWaiting(Long restaurantId, String username, int waitingNumber) {
        Member owner = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);

        validateRestaurantOwner(restaurant, owner);

        Waiting waiting = getWaitingByNumberAndRestaurant(waitingNumber, restaurant, WaitingStatus.STATUS_CALLED);

        waiting.updateStatus(WaitingStatus.STATUS_SEATED);
        stampRepository.save(new MemberStamp(waiting.getMember(), restaurant));
    }

    @Transactional
    public void callWaiting(Long restaurantId, String username, int waitingNumber) {
        Member owner = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);

        validateRestaurantOwner(restaurant, owner);

        Waiting waiting = getWaitingByNumberAndRestaurant(waitingNumber, restaurant, WaitingStatus.STATUS_WAITING);

        waiting.updateStatus(WaitingStatus.STATUS_CALLED);
    }

    @Transactional
    public void noShowWaiting(Long restaurantId, String username, int waitingNumber) {
        Member owner = getMemberByUsername(username);
        Restaurant restaurant = getRestaurantById(restaurantId);

        validateRestaurantOwner(restaurant, owner);

        Waiting waiting = getWaitingByNumberAndRestaurant(waitingNumber, restaurant, WaitingStatus.STATUS_WAITING);

        waiting.updateStatus(WaitingStatus.STATUS_NOSHOW);
    }

    @Transactional(readOnly = true)
    public OwnerWaitingListResponseDto getOwnerWaitingList(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        List<Waiting> calledWaitingList = waitingRepository.findByRestaurantAndWaitingStatus(restaurant, WaitingStatus.STATUS_CALLED);
        List<Waiting> waitingList = waitingRepository.findByRestaurantAndWaitingStatus(restaurant, WaitingStatus.STATUS_WAITING);

        List<WaitingUpdateResponseDto> calledWaitingDtos = calledWaitingList.stream()
                .map(WaitingUpdateResponseDto::of)
                .collect(Collectors.toList());

        List<WaitingUpdateResponseDto> waitingDtos = waitingList.stream()
                .map(WaitingUpdateResponseDto::of)
                .collect(Collectors.toList());

        return new OwnerWaitingListResponseDto(calledWaitingDtos, waitingDtos);
    }

    @Transactional(readOnly = true)
    public RestaurantResponseSummaryDto getTagInfo(WaitingNfcRequestDto requestDto) {
        Nfc nfc = getNfcById(requestDto.getNfcId());
        Restaurant restaurant = nfc.getRestaurant();
        Long currentWaitingNumber = waitingRepository.countByRestaurantAndWaitingStatus(nfc.getRestaurant(), WaitingStatus.STATUS_WAITING);

        return RestaurantResponseSummaryDto.of(restaurant, false, currentWaitingNumber, nfc.getRestaurant().getEstimatedTimePerTeam() * currentWaitingNumber);
    }


    private Member getMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }

    private Nfc getNfcById(String nfcId) {
        return nfcRepository.findByNfcId(nfcId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_NFC_NOT_FOUND));
    }

    private Restaurant getRestaurantById(Long restaurantId) {
        return restaurantRepository.findByRestaurantId(restaurantId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_RESTAURANT_NOT_FOUND));
    }

    private Waiting getWaitingByMemberAndRestaurant(Member member, Restaurant restaurant) {
        return waitingRepository.findByRestaurantAndMemberAndWaitingStatus(restaurant, member, WaitingStatus.STATUS_WAITING)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_GET_CURRENT_WAIT_POSITION));
    }

    private Waiting getWaitingByNumberAndRestaurant(int waitingNumber, Restaurant restaurant, WaitingStatus waitingStatus) {
        return waitingRepository.findByRestaurantAndWaitingNumberAndWaitingStatus(restaurant, waitingNumber, waitingStatus)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_GET_CURRENT_WAIT_POSITION));
    }

    private void validateDuplicateWaiting(Member member, Restaurant restaurant) {
        boolean alreadyWaiting = waitingRepository.existsByMemberAndRestaurantAndWaitingStatus(member, restaurant, WaitingStatus.STATUS_WAITING);
        if (alreadyWaiting) {
            throw new DuplicatedException(ResponseStatus.FAIL_MEMBER_WAITING_DUPLICATED);
        }
    }

    private void validateRestaurantOwner(Restaurant restaurant, Member owner) {
        if (!restaurant.getMember().equals(owner)) {
            throw new BusinessLogicException(ResponseStatus.FAIL_NOT_OWNER);
        }
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