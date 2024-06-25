package com.jungle.Tabbit.domain.notification.service;

import com.jungle.Tabbit.domain.fcm.dto.FcmRequestDto;
import com.jungle.Tabbit.domain.fcm.service.FcmService;
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

import java.io.IOException;

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

    private Member getMemberById(Long memberId) {
        return memberRepository.findMemberByMemberId(memberId)
                .orElseThrow(() -> new NotFoundException(ResponseStatus.FAIL_MEMBER_NOT_FOUND));
    }

}
