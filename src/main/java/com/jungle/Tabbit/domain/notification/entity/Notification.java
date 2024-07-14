package com.jungle.Tabbit.domain.notification.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "notification_id")
    private Long notificationId;

    @Column(nullable = false, name = "title")
    private String title;

    @Column(nullable = false, name = "message")
    private String message;

    @Column(nullable = false, name = "target")
    private String target;

    @Column(nullable = false, name = "messageType")
    private String messageType;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_read")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false, name = "data_only")
    private boolean dataOnly;

    public Notification(String title, String message, String target, String messageType, Member member, boolean dataOnly) {
        this.title = title;
        this.message = message;
        this.target = target;
        this.messageType = messageType;
        this.createdAt = LocalDateTime.now();
        this.isRead = false;
        this.member = member;
        this.dataOnly = dataOnly;
    }

    public void check() {
        this.isRead = true;
    }
}
