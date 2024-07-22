package com.jungle.Tabbit.domain.waiting.service;

import com.jungle.Tabbit.domain.fcm.dto.FcmData;
import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.nfc.entity.Nfc;
import com.jungle.Tabbit.domain.nfc.repository.NfcRepository;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.service.NotificationService;
import com.jungle.Tabbit.domain.order.dto.order.OrderMenuResponseDto;
import com.jungle.Tabbit.domain.order.repository.OrderMenuRepository;
import com.jungle.Tabbit.domain.order.service.OrderService;
import com.jungle.Tabbit.domain.restaurant.dto.RestaurantResponseSummaryDto;
import com.jungle.Tabbit.domain.restaurant.entity.Restaurant;
import com.jungle.Tabbit.domain.restaurant.repository.RestaurantRepository;
import com.jungle.Tabbit.domain.stampBadge.entity.MemberStamp;
import com.jungle.Tabbit.domain.stampBadge.repository.StampRepository;
import com.jungle.Tabbit.domain.stampBadge.service.BadgeTriggerService;
import com.jungle.Tabbit.domain.waiting.dto.*;
import com.jungle.Tabbit.domain.waiting.entity.Waiting;
import com.jungle.Tabbit.domain.waiting.entity.WaitingStatus;
import com.jungle.Tabbit.domain.waiting.repository.WaitingRepository;
import com.jungle.Tabbit.global.exception.BusinessLogicException;
import com.jungle.Tabbit.global.exception.DuplicatedException;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WaitingService {
    private final WaitingRepository waitingRepository;
    private final NfcRepository nfcRepository;
    private final MemberRepository memberRepository;
    private final RestaurantRepository restaurantRepository;
    private final StampRepository stampRepository;
    private final NotificationService notificationService;
    private final BadgeTriggerService badgeTriggerService;
    private final OrderService orderService;
    private final OrderMenuRepository orderMenuRepository;
    private static final ConcurrentHashMap<Long, AtomicLong> storeQueueNumbers = new ConcurrentHashMap<>();  // 가게별 전역 대기번호 변수

    private static final Logger logger = LoggerFactory.getLogger(WaitingService.class);


    @Transactional(isolation = Isolation.SERIALIZABLE)
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

        sendRegistrationNotification(member, restaurant, waiting, currentWaitingPosition, estimatedWaitTime, queueNumber);

        return WaitingResponseDto.of(waiting, estimatedWaitTime, currentWaitingPosition);
    }

    @Transactional(readOnly = true)
    public WaitingResponseDto getWaitingOverview(Long waitingId) {
        Waiting waiting = getWaitingById(waitingId);
        int currentWaitingPosition = getCurrentWaitingPosition(waiting);
        Long estimatedWaitTime = calculateEstimatedWaitTime(currentWaitingPosition, waiting.getRestaurant().getEstimatedTimePerTeam());
        return WaitingResponseDto.of(waiting, estimatedWaitTime, currentWaitingPosition);
    }

    @Transactional(readOnly = true)
    public WaitingListResponseDto getUserWaitingList(String username) {
        Member member = getMemberByUsername(username);
        List<Waiting> waitingList = waitingRepository.findByMemberAndWaitingStatusIn(
                member, Arrays.asList(WaitingStatus.STATUS_WAITING, WaitingStatus.STATUS_CALLED));

        List<WaitingResponseDto> waitingResponseDtos = waitingList.stream()
                .sorted(Comparator.comparing(Waiting::getCreatedAt).reversed())
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
    public void cancelWaiting(Long waitingId, String username) {
        Waiting waiting = getWaitingById(waitingId);
        Member member = getMemberByUsername(username);

        waiting.updateStatus(WaitingStatus.STATUS_CANCELLED);
        orderService.deleteOrderIfExists(waiting.getWaitingId());

        sendCancellationNotification(member, waiting.getRestaurant(), waiting);

        notifyImminentEntryToWaiters(waiting.getRestaurant(), waiting);
    }

    @Transactional
    public void confirmWaiting(Long waitingId, String username) {
        Waiting waiting = getWaitingById(waitingId);
        Member owner = getMemberByUsername(username);
        Restaurant restaurant = waiting.getRestaurant();

        validateRestaurantOwner(restaurant, owner);

        waiting.updateStatus(WaitingStatus.STATUS_SEATED);
        Optional<MemberStamp> memberStamp = stampRepository.findByMemberAndRestaurant(waiting.getMember(), restaurant);
        if (memberStamp.isPresent()) {
            memberStamp.get().updateVisitCount(memberStamp.get().getVisitCount());
            stampRepository.save(memberStamp.get());
        } else {
            stampRepository.save(new MemberStamp(waiting.getMember(), restaurant, restaurant.getCategory()));
            sendNotification(waiting.getMember().getMemberId(),
                    "스탬프 획득", restaurant.getName() + " 스탬프를 획득하였습니다.",
                    createFcmData("client", "confirm", restaurant, waiting));
        }

        badgeTriggerService.checkAndAwardBadges(waiting.getMember());
        sendNotification(waiting.getMember().getMemberId(),
                "입장 완료", restaurant.getName() + " 입장완료 하였습니다.",
                createFcmData("client", "confirm", restaurant, waiting), true);
    }

    @Transactional
    public void callWaiting(Long waitingId, String username) {
        Waiting waiting = getWaitingById(waitingId);
        Member owner = getMemberByUsername(username);
        Restaurant restaurant = waiting.getRestaurant();

        validateRestaurantOwner(restaurant, owner);

        waiting.updateStatus(WaitingStatus.STATUS_CALLED);

        sendNotification(waiting.getMember().getMemberId(),
                "입장 알림", "입장 차례가 되었습니다. 가게로 입장해 주세요. 5분이내 입장하지 않을 시 자동 취소됩니다.",
                createFcmData("client", "call", restaurant, waiting));

        notifyImminentEntryToWaiters(restaurant, waiting);
        orderService.updateOrderStatusToConfirmed(waiting.getMember(), restaurant);
    }

    @Transactional
    public void noShowWaiting(Long waitingId, String username) {
        Waiting waiting = getWaitingById(waitingId);
        Member owner = getMemberByUsername(username);
        Restaurant restaurant = waiting.getRestaurant();

        validateRestaurantOwner(restaurant, owner);

        waiting.updateStatus(WaitingStatus.STATUS_NOSHOW);

        sendNotification(waiting.getMember().getMemberId(), "No-Show 처리 알림", "No-Show로 처리되었습니다.", createFcmData("client", "noshow", restaurant, waiting));
    }

    @Transactional(readOnly = true)
    public OwnerWaitingListResponseDto getOwnerWaitingList(Long restaurantId) {
        Restaurant restaurant = getRestaurantById(restaurantId);
        List<Waiting> calledWaitingList = waitingRepository.findByRestaurantAndWaitingStatus(restaurant, WaitingStatus.STATUS_CALLED);
        List<Waiting> waitingList = waitingRepository.findByRestaurantAndWaitingStatus(restaurant, WaitingStatus.STATUS_WAITING);

        List<WaitingWithOrderDto> calledWaitingDtos = mapWaitingsToDtos(calledWaitingList);
        List<WaitingWithOrderDto> waitingDtos = mapWaitingsToDtos(waitingList);

        return OwnerWaitingListResponseDto.builder()
                .estimatedWaitTime(restaurant.getEstimatedTimePerTeam())
                .calledWaitingList(calledWaitingDtos)
                .waitingList(waitingDtos)
                .build();
    }

    private List<WaitingWithOrderDto> mapWaitingsToDtos(List<Waiting> waitings) {
        return waitings.stream()
                .map(waiting -> {
                    List<OrderMenuResponseDto> menuItems = getOrderMenuDtosForWaiting(waiting);
                    return WaitingWithOrderDto.builder()
                            .waiting(WaitingUpdateResponseDto.of(waiting))
                            .menuItems(menuItems)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private List<OrderMenuResponseDto> getOrderMenuDtosForWaiting(Waiting waiting) {
        return orderMenuRepository.findByOrder_Waiting(waiting).stream()
                .map(orderMenu -> OrderMenuResponseDto.builder()
                        .menuId(orderMenu.getMenu().getMenuId())
                        .menuName(orderMenu.getMenu().getName())
                        .quantity(orderMenu.getQuantity())
                        .price(orderMenu.getMenu().getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RestaurantResponseSummaryDto getTagInfo(WaitingNfcRequestDto requestDto) {
        Nfc nfc = getNfcById(requestDto.getNfcId());
        Restaurant restaurant = nfc.getRestaurant();
        Long currentWaitingNumber = waitingRepository.countByRestaurantAndWaitingStatus(restaurant, WaitingStatus.STATUS_WAITING);

        return RestaurantResponseSummaryDto.of(restaurant, false, currentWaitingNumber, restaurant.getEstimatedTimePerTeam() * currentWaitingNumber);
    }

    private Waiting getWaitingById(Long waitingId) {
        return waitingRepository.findByWaitingId(waitingId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_GET_CURRENT_WAIT_POSITION));
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

    private void validateDuplicateWaiting(Member member, Restaurant restaurant) {
        boolean alreadyWaiting = waitingRepository.existsByMemberAndRestaurantAndWaitingStatusIn(member, restaurant, Arrays.asList(WaitingStatus.STATUS_WAITING, WaitingStatus.STATUS_CALLED));
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
        if (waiting.getWaitingStatus() == WaitingStatus.STATUS_CALLED || waiting.getWaitingStatus() == WaitingStatus.STATUS_SEATED || waiting.getWaitingStatus() == WaitingStatus.STATUS_CANCELLED) {
            return 0;  // called 상태이면 0 반환
        }

        List<Waiting> waitingList = waitingRepository.findByRestaurantAndWaitingStatusOrderByWaitingNumberAsc(waiting.getRestaurant(), WaitingStatus.STATUS_WAITING);
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getWaitingId().equals(waiting.getWaitingId())) {
                return i + 1;
            }
        }
        throw new BusinessLogicException(ResponseStatus.FAIL_MEMBER_WAITING_DUPLICATED);
    }

    private Long calculateEstimatedWaitTime(int position, Long estimatedTimePerTeam) {
        return position * estimatedTimePerTeam;
    }

    private void sendNotification(Long memberId, String title, String message, FcmData data, boolean flag) {
        NotificationRequestCreateDto notificationRequest = NotificationRequestCreateDto.builder()
                .memberId(memberId)
                .title(title)
                .message(message)
                .fcmData(data)
                .build();
        notificationService.sendNotification(notificationRequest, flag);
    }

    private void sendNotification(Long memberId, String title, String message, FcmData data) {
        sendNotification(memberId, title, message, data, false);
    }

    private FcmData createFcmData(String role, String messageType, Restaurant restaurant, Waiting waiting) {
        return FcmData.builder()
                .target(role)
                .messageType(messageType)
                .restaurantId(String.valueOf(restaurant.getRestaurantId()))
                .restaurantName(restaurant.getName())
                .waitingNumber(String.valueOf(waiting.getWaitingNumber()))
                .build();
    }

    private void sendRegistrationNotification(Member member, Restaurant restaurant, Waiting waiting, int currentWaitingPosition, Long estimatedWaitTime, Long queueNumber) {
        FcmData clientData = createFcmData("client", "register", restaurant, waiting);
        sendNotification(member.getMemberId(), "웨이팅 등록 완료 알림",
                "웨이팅이 성공적으로 등록되었습니다. 현재 대기 순서는 " + currentWaitingPosition + "번째이며, 예상 대기시간은 " + estimatedWaitTime + "분입니다.", clientData);

        FcmData ownerData = createFcmData("owner", "register", restaurant, waiting);
        sendNotification(restaurant.getMember().getMemberId(), "새로운 웨이팅 알림",
                "새로운 웨이팅이 등록되었습니다. 대기번호는 " + queueNumber + "번입니다.", ownerData);
    }

    private void sendCancellationNotification(Member member, Restaurant restaurant, Waiting waiting) {
        FcmData clientData = createFcmData("client", "cancel", restaurant, waiting);
        sendNotification(member.getMemberId(), "웨이팅 취소 알림", "웨이팅이 성공적으로 취소되었습니다.", clientData);

        FcmData ownerData = createFcmData("owner", "cancel", restaurant, waiting);
        sendNotification(restaurant.getMember().getMemberId(), "웨이팅 취소 알림", "웨이팅이 취소되었습니다.", ownerData);
    }

//    @Async
//    public void notifyImminentEntryToWaitersAsync(Restaurant restaurant, Waiting waiting) {
//        long start = System.currentTimeMillis();
//        List<Waiting> waitingList = waitingRepository.findByRestaurantAndWaitingStatusOrderByWaitingNumberAsc(restaurant, WaitingStatus.STATUS_WAITING);
//        for (int i = 0; i < waitingList.size(); i++) {
//            Waiting nextWaiting = waitingList.get(i);
//            sendImminentEntryNotification(nextWaiting.getMember(), i, restaurant, waiting);
//        }
//        long executionTime = System.currentTimeMillis() - start;
//        logger.info("notifyImminentEntryToWaiters executed in {}ms", executionTime);
//    }

    @Async
    public void notifyImminentEntryToWaitersAsync(Restaurant restaurant, Waiting waiting) {
        long start = System.currentTimeMillis();

        List<Waiting> waitingList = waitingRepository.findByRestaurantAndWaitingStatusOrderByWaitingNumberAsc(restaurant, WaitingStatus.STATUS_WAITING);
        waitingList.parallelStream().forEach(nextWaiting -> {
            int index = waitingList.indexOf(nextWaiting);
            long notificationStart = System.currentTimeMillis();
            sendImminentEntryNotification(nextWaiting.getMember(), index, restaurant, waiting);
            long notificationEnd = System.currentTimeMillis();
            System.out.println("Notification sent to " + nextWaiting.getMember().getUsername() + " in " + (notificationEnd - notificationStart) + "ms");
        });

        long end = System.currentTimeMillis();
        System.out.println("notifyImminentEntryToWaitersAsync executed in " + (end - start) + "ms");
    }


    public void notifyImminentEntryToWaiters(Restaurant restaurant, Waiting waiting) {
        notifyImminentEntryToWaitersAsync(restaurant, waiting);
    }

    private void sendImminentEntryNotification(Member member, int currentWaitingPosition, Restaurant restaurant, Waiting waiting) {
        String message = getImminentEntryMessage(currentWaitingPosition);
        if (!message.isEmpty()) {
            FcmData data = createFcmData("client", "imminent", restaurant, waiting);
            sendNotification(member.getMemberId(), "입장 임박 알림", message, data);
        }
    }

    private String getImminentEntryMessage(int currentWaitingPosition) {
        switch (currentWaitingPosition) {
            case 2:
                return "입장이 임박했습니다. 현재 대기 순서는 3번째입니다.";
            case 1:
                return "입장이 임박했습니다. 현재 대기 순서는 2번째입니다.";
            case 0:
                return "입장이 임박했습니다. 현재 대기 순서는 1번째입니다. 가게 주변에서 대기해주세요";
            default:
                return "";
        }
    }

    @Scheduled(cron = "0 0 5 * * *")
    @Transactional
    public void resetQueueNumbers() {
        storeQueueNumbers.replaceAll((storeId, queueNumber) -> new AtomicLong(0));
        List<Waiting> waitingList = waitingRepository.findAllByWaitingStatus(WaitingStatus.STATUS_WAITING);
        List<Waiting> calledList = waitingRepository.findAllByWaitingStatus(WaitingStatus.STATUS_CALLED);

        for (Waiting waiting : waitingList) {
            waiting.updateStatus(WaitingStatus.STATUS_CANCELLED);
        }

        for (Waiting waiting : calledList) {
            Restaurant restaurant = waiting.getRestaurant();
            waiting.updateStatus(WaitingStatus.STATUS_SEATED);
            Optional<MemberStamp> memberStamp = stampRepository.findByMemberAndRestaurant(waiting.getMember(), restaurant);
            if (memberStamp.isPresent()) {
                memberStamp.get().updateVisitCount(memberStamp.get().getVisitCount());
                stampRepository.save(memberStamp.get());
            } else {
                stampRepository.save(new MemberStamp(waiting.getMember(), restaurant, restaurant.getCategory()));
            }
            badgeTriggerService.checkAndAwardBadges(waiting.getMember());
        }
    }
}
