package com.jungle.Tabbit.domain.notification.repository;

import com.jungle.Tabbit.domain.member.entity.Member;
import com.jungle.Tabbit.domain.notification.entity.Notification;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends Repository<Notification, Long> {
    void save(Notification notification);

    List<Notification> findAllByMemberAndDataOnlyFalse(Member member);

    Optional<Notification> findNotificationByNotificationId(Long notificationId);

    void deleteAllByMember(Member member);

    Optional<Notification> findNotificationByNotificationIdAndMember(Long notificationId, Member member);

    void delete(Notification notification);
}
