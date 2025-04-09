package com.jungle.Tabbit.global.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class NotificationExecutionTimeAspect {
    @Around("execution(* com.jungle.Tabbit.domain.notification.service.NotificationService.sendNotification(..)) || " +
            "execution(* com.jungle.Tabbit.domain.waiting.service.WaitingService.cancelWaiting(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed(); // 원래 메서드 실행

        long executionTime = System.currentTimeMillis() - start;

        log.info("[알림 전송 시간] {} 메서드 실행 시간: {}ms", joinPoint.getSignature(), executionTime);

        return proceed;
    }
}
