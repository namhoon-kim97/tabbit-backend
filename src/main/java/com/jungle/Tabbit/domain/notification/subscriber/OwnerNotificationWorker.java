package com.jungle.Tabbit.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.service.NotificationService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OwnerNotificationWorker implements
        StreamListener<String, MapRecord<String, String, String>>, InitializingBean {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    @Qualifier("taskExecutor")
    private final Executor taskExecutor;

    private StreamMessageListenerContainer<String, MapRecord<String, String, String>> listenerContainer;

    private static final String STREAM_KEY = "stream:notifications:owner";
    private static final String GROUP = "notification-owner";
    private static final String CONSUMER_NAME = "owner-worker-1";

    @Override
    public void afterPropertiesSet() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.latest(), GROUP);
        } catch (Exception e) {
            log.info("Consumer group already exists: {}", GROUP);
        }

        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .<String, MapRecord<String, String, String>>builder()
                .pollTimeout(Duration.ofSeconds(2))
                .executor(taskExecutor)
                .errorHandler(this::handleStreamError)  // 에러 핸들러 추가
                .build();

        listenerContainer = StreamMessageListenerContainer.create(redisTemplate.getConnectionFactory(), options);

        listenerContainer.receive(
                Consumer.from(GROUP, CONSUMER_NAME),
                StreamOffset.create(STREAM_KEY, ReadOffset.lastConsumed()),
                this
        );

        listenerContainer.start();
        log.info("OwnerNotificationListener started");
    }

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        String recordId = message.getId().getValue();
        try {
            Map<String, String> value = message.getValue();

            // null 체크 추가
            if (value == null || value.isEmpty()) {
                log.warn("빈 메시지 수신 - recordId: {}", recordId);
                ackSafe(message);
                return;
            }

            String jsonPayload = value.get("payload");
            if (jsonPayload == null || jsonPayload.trim().isEmpty()) {
                log.error("payload가 비어있습니다 - recordId: {}, value: {}", recordId, value);
                ackSafe(message);
                return;
            }

            NotificationRequestCreateDto dto = objectMapper.readValue(
                    jsonPayload, NotificationRequestCreateDto.class
            );

            taskExecutor.execute(() -> {
                try {
                    notificationService.sendNotification(dto, false);
                    Long ackResult = redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, message.getId());
                    log.info("Owner ACK 결과: {} (ID: {})", ackResult, message.getId());
                } catch (Exception e) {
                    log.error("Owner 알림 처리 실패 - recordId: {}", message.getId(), e);
                    NotificationFailureLogger.log(recordId, dto, e);
                }
            });

        } catch (Exception e) {
            log.error("Owner DTO 파싱 실패 - recordId: {}", message.getId(), e);
        }
    }

    private void ackSafe(MapRecord<String, String, String> message) {
        try {
            redisTemplate.opsForStream().acknowledge(STREAM_KEY, GROUP, message.getId());
        } catch (Exception ex) {
            log.error("ACK 실패 - recordId: {}", message.getId(), ex);
        }
    }

    // 에러 핸들러 메서드 추가
    private void handleStreamError(Throwable throwable) {
        if (throwable instanceof ConversionFailedException) {
            log.warn("Stream 메시지 변환 실패 (null 값으로 인한 문제일 가능성): {}", throwable.getMessage());
            // 메시지를 스킵하고 계속 진행
            return;
        }
        log.error("Stream 처리 중 예상치 못한 에러 발생", throwable);
    }

    @PreDestroy
    public void shutdown() {
        if (listenerContainer != null) {
            listenerContainer.stop();
            log.info("🛑 OwnerNotificationListener 종료 완료");
        }
    }
}
