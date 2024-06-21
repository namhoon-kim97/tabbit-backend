package com.jungle.Tabbit.domain.waiting.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.nfc.entity.Nfc;
import com.jungle.Tabbit.domain.nfc.repository.NfcRepository;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.waiting.dto.WaitingRequestCreateDto;
import com.jungle.Tabbit.domain.waiting.dto.WaitingResponseDto;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import com.jungle.Tabbit.domain.waiting.repository.WaitingRepository;
import com.jungle.Tabbit.global.exception.BusinessLogicException;
import com.jungle.Tabbit.global.exception.DuplicatedException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import com.jungle.Tabbit.global.util.SecurityUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final NfcRepository nfcRepository;
    private final MemberRepository memberRepository;
    private static final ConcurrentHashMap<Long, AtomicLong> storeQueueNumbers = new ConcurrentHashMap<>();  // 가게별 전역 대기번호 변수

    @Transactional
    public WaitingResponseDto registerWaiting(WaitingRequestCreateDto requestDto) {
        String username = SecurityUtil.getCurrentUsername();
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

    private Long getNextQueueNumber(Long storeId) {
        storeQueueNumbers.putIfAbsent(storeId, new AtomicLong(0));
        return storeQueueNumbers.get(storeId).incrementAndGet();
    }

    public int getCurrentWaitingPosition(Waiting waiting) {
        List<Waiting> waitingList = waitingRepository.findByRestaurantAndWaitingStatusOrderByWaitingNumberAsc(waiting.getRestaurant(), WaitingStatus.STATUS_WAITING);
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getWaitingId().equals(waiting.getWaitingId())) {
                return i;
            }
        }
        throw new BusinessLogicException(ResponseStatus.FAIL_MEMBER_WAITING_DUPLICATED);
    }

    public Long calculateEstimatedWaitTime(int position, Long estimatedTimePerTeam) {
        return position * estimatedTimePerTeam;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetQueueNumbers() {
        storeQueueNumbers.replaceAll((storeId, queueNumber) -> new AtomicLong(0));
    }
}