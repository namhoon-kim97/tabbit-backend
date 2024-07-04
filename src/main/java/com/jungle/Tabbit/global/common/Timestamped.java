package com.jungle.Tabbit.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    public Boolean isRecent() {
        LocalDateTime now = LocalDateTime.now();
        long daysSinceCreated = Duration.between(createdAt, now).toDays();
        if (updatedAt == null) {
            return daysSinceCreated < 3;
        }
        long daysSinceUpdated = Duration.between(updatedAt, now).toDays();
        return daysSinceCreated < 3 || daysSinceUpdated < 3;
    }
}
