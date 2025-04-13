package com.jungle.Tabbit.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;

import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSubscriber implements MessageListener {

    private final ObjectMapper objectMapper;
    private final NotificationHandler notificationHandler;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String body = new String(message.getBody(), StandardCharsets.UTF_8);
            NotificationRequestCreateDto dto = objectMapper.readValue(body, NotificationRequestCreateDto.class);

            notificationHandler.handle(dto);  // 여기서 비동기 알림 전송
        } catch (Exception e) {
            log.error("Redis PubSub 알림 수신 실패", e);
        }
    }
}

