package com.jungle.Tabbit.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;

import com.jungle.Tabbit.domain.notification.service.NotificationService;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.stream.*;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationSubscriber  {

//    private final ObjectMapper objectMapper;
//    private final NotificationHandler notificationHandler;
//    private final RedisTemplate<String, Object> redisTemplate; // DLQ 저장용
//
//    private static final String DEAD_LETTER_QUEUE = "waiting:notification:dlq"; // DLQ 키
//
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            String body = new String(message.getBody(), StandardCharsets.UTF_8);
//            NotificationRequestCreateDto dto = objectMapper.readValue(body, NotificationRequestCreateDto.class);
//
//            notificationHandler.handle(dto);  // 여기서 비동기 알림 전송
//        } catch (Exception e) {
//            log.error("Redis PubSub 알림 수신 실패", e);
//            moveToDeadLetterQueue(message, e);
//        }
//    }
//    private void moveToDeadLetterQueue(Message message, Exception e) {
//        try {
//            String body = new String(message.getBody(), StandardCharsets.UTF_8);
//
//            // 실패한 메시지 + 실패 이유 + 시간 기록
//            FailedNotification failed = new FailedNotification(
//                    body,
//                    e.getMessage(),
//                    Instant.now().toString()
//            );
//
//            redisTemplate.opsForList().leftPush(DEAD_LETTER_QUEUE, failed);
//            log.info("메시지를 DLQ에 저장했습니다.");
//        } catch (Exception ex) {
//            log.error("DLQ 저장 실패", ex);
//        }
//    }
    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    private static final String STREAM_KEY = "stream:notifications";
    private static final String GROUP = "notification-group";
    private static final String CONSUMER_NAME = "worker-1";

    // 2초마다 최대 10개씩 읽어서 처리
    @Scheduled(fixedDelay = 2000)
    public void pollStream() {
        List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream().read(
                Consumer.from(GROUP, CONSUMER_NAME),
                StreamReadOptions.empty().count(10).block(Duration.ofMillis(1000)),
                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
        );

        for (MapRecord<String, Object, Object> record : messages) {
            try {
                Map<Object, Object> rawData = record.getValue();
                NotificationRequestCreateDto dto = objectMapper.convertValue(rawData, NotificationRequestCreateDto.class);
                notificationService.sendNotification(dto, false);
                redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, record.getId());
            } catch (Exception e) {
                log.error("알림 전송 실패", e);
                // ack 안 하면 pending 처리됨
            }
        }
    }
}

