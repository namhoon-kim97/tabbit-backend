package com.jungle.Tabbit.domain.notification.entity;

import com.jungle.Tabbit.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "is_read")
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Member member;

    public Notification(String title, String message, Member member) {
        this.title = title;
        this.message = message;
        this.member = member;
        this.isRead = false;
    }



}
