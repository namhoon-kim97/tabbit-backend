package com.jungle.Tabbit.domain.notification.dto;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.notification.entity.Notification;
import com.jungle.example_code.domain.board.dto.BoardResponseDto;
import com.jungle.example_code.domain.board.entity.Board;
import com.jungle.example_code.domain.reply.dto.ReplyResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class NotificationListResponseDto {

    private Long memberId;
    private List<NotificationResponseDto> notificationList; ;

    public static NotificationListResponseDto of(Long memberId, List<Notification> notificationList) {
        List<NotificationResponseDto> notifications = notificationList.stream()
                .map(NotificationResponseDto::of)
                .collect(Collectors.toList());

        return NotificationListResponseDto.builder()
                .memberId(memberId)
                .notificationList(notifications)
                .build();
    }
}
