package com.jungle.Tabbit.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.service.NotificationService;
import jakarta.annotation.PostConstruct;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OwnerNotificationWorker {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;

    private static final String STREAM_KEY = "stream:notifications";
    private static final String GROUP = "notification-owner";
    private static final String CONSUMER_NAME = "owner-worker-1";

    @PostConstruct
    @Async("notificationExecutor")
    public void listenOwnerNotifications() {
        log.info("OwnerNotificationWorker 시작됨");

        while (true) {
            try {
                List<MapRecord<String, Object, Object>> messages = redisTemplate.opsForStream().read(
                        Consumer.from(GROUP, CONSUMER_NAME),
                        StreamReadOptions.empty().count(10).block(Duration.ofSeconds(1)),
                        StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed())
                );

                if (messages == null || messages.isEmpty()) continue;

                for (MapRecord<String, Object, Object> record : messages) {
                    try {
                        Map<Object, Object> rawData = record.getValue();
                        NotificationRequestCreateDto dto = objectMapper.convertValue(rawData, NotificationRequestCreateDto.class);

                        if (!"owner".equals(dto.getFcmData().getTarget())) continue;

                        notificationService.sendNotification(dto, false);
                        redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, record.getId());

                    } catch (Exception e) {
                        log.error("알림 전송 실패 [owner] - recordId: {}", record.getId(), e);
                    }
                }

            } catch (Exception e) {
                log.error("Redis 읽기 오류 [owner]", e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}


