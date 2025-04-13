package com.jungle.Tabbit.domain.notification.publisher;

import com.jungle.Tabbit.domain.notification.dto.NotificationRequestCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic topic;

    @Async("taskExecutor")
    public void publish(NotificationRequestCreateDto requestDto) {
        redisTemplate.convertAndSend(topic.getTopic(), requestDto);
    }
}