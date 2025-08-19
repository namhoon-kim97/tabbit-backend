package com.jungle.Tabbit.domain.notification.publisher;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.connection.stream.StringRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String STREAM_CLIENT = "stream:notifications:client";
    private static final String STREAM_OWNER  = "stream:notifications:owner";

    public void publish(NotificationRequestCreateDto dto) {
        try {
            String jsonData = objectMapper.writeValueAsString(dto);

            // Map으로 데이터 구성 (null 값 방지)
            Map<String, String> streamData = new HashMap<>();
            streamData.put("payload", jsonData);

            String target = dto.getFcmData() != null ? dto.getFcmData().getTarget() : null;
            String streamKey = switch (target) {
                case "client" -> STREAM_CLIENT;
                case "owner"  -> STREAM_OWNER;
                default -> throw new IllegalStateException("Unexpected value: " + target);
            };

            redisTemplate.opsForStream().add(
                    StreamRecords.mapBacked(streamData).withStreamKey(streamKey)
            );

            log.debug("알림 발행 완료: {}", dto);
        } catch (Exception e) {
            log.error("Redis Stream 발행 실패: {}", dto, e);
            throw new IllegalArgumentException("Redis Stream 직렬화 실패", e);
        }
    }
}