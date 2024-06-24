package com.jungle.Tabbit.domain.notification.repository;

import com.jungle.Tabbit.domain.notification.entity.Notification;
import org.springframework.data.repository.Repository;

public interface NotificationRepository extends Repository<Notification, Long> {
    void save(Notification notification);
}
