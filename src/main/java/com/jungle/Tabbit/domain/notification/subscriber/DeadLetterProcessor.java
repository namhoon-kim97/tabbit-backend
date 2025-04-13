package com.jungle.Tabbit.domain.notification.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeadLetterProcessor {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;
    private final ObjectMapper objectMapper;

    private static final String DEAD_LETTER_QUEUE = "waiting:notification:dlq";
    private static final long MAX_RETRY_WINDOW_SECONDS = 30; // 30초 이상 지난 메시지는 버리기

    @Scheduled(fixedDelay = 10000) // 10초마다
    public void retryFailedMessages() {
        try {
            Object failed = redisTemplate.opsForList().rightPop(DEAD_LETTER_QUEUE);
            if (failed == null) return;

            FailedNotification failedNotification = objectMapper.convertValue(failed, FailedNotification.class);
            String originalMessage = failedNotification.getOriginalMessage();

            Instant failedAt = Instant.parse(failedNotification.getFailedAt());
            Instant now = Instant.now();
            long elapsedSeconds = Duration.between(failedAt, now).getSeconds();

            if (elapsedSeconds > MAX_RETRY_WINDOW_SECONDS) {
                // 30초 초과 -> 이 알림은 의미가 없으므로 버림
                log.warn("DLQ 재처리 포기: 생성된 지 {}초 지난 메시지", elapsedSeconds);
                return;
            }

            // 30초 안 지났으면 정상 채널에 다시 publish
            NotificationRequestCreateDto dto = objectMapper.readValue(originalMessage, NotificationRequestCreateDto.class);
            redisTemplate.convertAndSend(topic.getTopic(), dto);
            log.info("DLQ 재처리 성공: 메시지를 다시 발행했습니다.");

        } catch (Exception e) {
            log.error("DLQ 재처리 실패", e);
        }
    }
}
