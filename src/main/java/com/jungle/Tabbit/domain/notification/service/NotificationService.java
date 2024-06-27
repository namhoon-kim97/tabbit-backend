package com.jungle.Tabbit.domain.notification.service;

import com.jungle.Tabbit.domain.fcm.dto.FcmRequestDto;
import com.jungle.Tabbit.domain.fcm.service.FcmService;
import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.notification.dto.NotificationListResponseDto;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.entity.Notification;
import com.jungle.Tabbit.domain.notification.repository.NotificationRepository;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final FcmService fcmService;

    public void sendNotification(NotificationRequestCreateDto requestDto)  {
        Member member = getMemberById(requestDto.getMemberId());

        FcmRequestDto fcmRequestDto = FcmRequestDto.builder()
                .token(member.getFcmToken())
                .title(requestDto.getTitle())
                .body(requestDto.getMessage())
                .data(requestDto.getFcmData())
                .build();

        fcmService.sendMessageTo(fcmRequestDto);

        notificationRepository.save(new Notification(requestDto.getTitle(), requestDto.getMessage(), member));
    }
    public NotificationListResponseDto getNotificationList(Long userId) {
        Member member = getMemberById(userId);
        return NotificationListResponseDto.of(userId, notificationRepository.findAllByMember(member));
    }



    public void checkNotification(Long notificationId) {
        Notification notification = notificationRepository.findNotificationByNotificationId(notificationId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_NOTIFICATION_NOT_FOUND));
        notification.check();
    }


    public void deleteALLNotification(Long userId) {
        Member member = getMemberById(userId);
        notificationRepository.deleteAllByMember(member);
    }


    public void deleteNotification(Long userId, Long notificationId) {
        Member member = getMemberById(userId);
        Notification notification = notificationRepository.findNotificationByNotificationIdAndMember(notificationId, member)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_NOTIFICATION_NOT_FOUND));

        notificationRepository.delete(notification);
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }

}
