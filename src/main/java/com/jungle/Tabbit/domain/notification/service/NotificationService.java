package com.jungle.Tabbit.domain.notification.service;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.member.repository.MemberRepository;
import com.jungle.Tabbit.domain.member.service.MemberService;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.dto.NotificationResponseDto;
import com.jungle.Tabbit.domain.notification.entity.Notification;
import com.jungle.Tabbit.domain.notification.repository.NotificationRepository;
import com.jungle.Tabbit.global.exception.NotFoundException;
import com.jungle.Tabbit.global.model.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;

    public void sendNotification(NotificationRequestCreateDto request,String username) {
        Member member = getMemberByUsername(username);

        // 비즈니스 로직 수행 및 알림 생성
        Notification notification = new Notification(request.getTitle(), request.getMessage(), member);
        notificationRepository.save(notification);

        // 특정 사용자의 구독 경로로 알림 전송
        messagingTemplate.convertAndSend("/topic/notifications/" + request.getMemberId(), NotificationResponseDto.of(notification));
    }
    private Member getMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }

}
