package com.jungle.Tabbit.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.service.NotificationService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
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
public class ClientNotificationWorker {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    @Qualifier("taskExecutor")
    private final Executor taskExecutor;
    private volatile boolean running = true; // 종료 시 false로 전환

    private static final String STREAM_KEY = "stream:notifications";
    private static final String GROUP = "notification-client";
    private static final String CONSUMER_NAME = "client-worker-1";
    @PreDestroy
    public void shutdown() {
        log.info("OwnerNotificationWorker 종료 요청");
        running = false;
    }
    @EventListener(ApplicationReadyEvent.class)
    @Async("taskExecutor")
    public void listenClientNotifications() {
        log.info("ClientNotificationWorker 시작됨");

        while (running) {
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

                        if (!"client".equals(dto.getFcmData().getTarget())) continue;

                        taskExecutor.execute(() -> {
                            try {
                                notificationService.sendNotification(dto, false);
                                redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, record.getId());
                            } catch (Exception e) {
                                log.error("병렬 알림 전송 실패 [client] - recordId: {}", record.getId(), e);
                            }
                        });

                    } catch (Exception e) {
                        log.error("DTO 파싱 실패 [client] - recordId: {}", record.getId(), e);
                    }
                }

            } catch (Exception e) {
                log.error("Redis 읽기 오류 [client]", e);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}