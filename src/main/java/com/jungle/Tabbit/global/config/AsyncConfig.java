package com.jungle.Tabbit.global.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(20);    // 항상 대기하는 스레드 수
        executor.setMaxPoolSize(100);    // 최대 스레드 수
        executor.setQueueCapacity(200);  // 대기할 수 있는 작업 큐 용량
        executor.setThreadNamePrefix("Async-Notification-");
        executor.initialize();
        return executor;
    }
}
