package com.jungle.Tabbit.domain.notification.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {

//    private final RedisTemplate<String, Object> redisTemplate;
//    private final ChannelTopic topic;
//
//    @Async("taskExecutor")
//    public void publish(NotificationRequestCreateDto requestDto) {
//        redisTemplate.convertAndSend(topic.getTopic(), requestDto);
//    }
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String STREAM_KEY = "stream:notifications";

    public void publish(NotificationRequestCreateDto dto) {
        redisTemplate.opsForStream().add(
                ObjectRecord.create(STREAM_KEY, dto)
        );
    }
}