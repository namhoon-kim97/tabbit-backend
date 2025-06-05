package com.jungle.Tabbit.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import com.jungle.Tabbit.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import jakarta.annotation.PreDestroy;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.Executor;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OwnerNotificationWorker implements StreamListener<String, ObjectRecord<String, Map>>, InitializingBean {

    private final RedisTemplate<String, Object> redisTemplate;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    @Qualifier("taskExecutor")
    private final Executor taskExecutor;

    private StreamMessageListenerContainer<String, ObjectRecord<String, Map>> listenerContainer;

    private static final String STREAM_KEY = "stream:notifications";
    private static final String GROUP = "notification-owner";
    private static final String CONSUMER_NAME = "owner-worker-1";

    @Override
    public void afterPropertiesSet() {
        try {
            redisTemplate.opsForStream().createGroup(STREAM_KEY, ReadOffset.from("0"), GROUP);
        } catch (Exception e) {
            log.info("Consumer group already exists: {}", GROUP);
        }

        var options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .<String, ObjectRecord<String, Map>>builder()
                .pollTimeout(Duration.ofSeconds(2))
                .targetType(Map.class)
                .executor(taskExecutor)
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
    public void onMessage(ObjectRecord<String, Map> message) {
        String recordId = message.getId().getValue();
        try {
            NotificationRequestCreateDto dto = objectMapper.convertValue(message.getValue(), NotificationRequestCreateDto.class);

            if (!"owner".equals(dto.getFcmData().getTarget())) return;

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

    @PreDestroy
    public void shutdown() {
        if (listenerContainer != null) {
            listenerContainer.stop();
            log.info("🛑 OwnerNotificationListener 종료 완료");
        }
    }
}
