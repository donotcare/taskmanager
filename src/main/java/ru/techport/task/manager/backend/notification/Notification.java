package ru.techport.task.manager.backend.notification;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Immutable
public class Notification {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime notificationDate;
    @Enumerated(EnumType.STRING)
    private NotificationType type;

    public Notification() {

    }

    public Notification(LocalDateTime notificationDate) {
    }

    private Notification(LocalDateTime notificationDate, NotificationType type) {
        this.notificationDate = notificationDate;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getNotificationDate() {
        return notificationDate;
    }

    public NotificationType getType() {
        return type;
    }

    public static Notification of(LocalDateTime notificationDate, NotificationType type) {
        return new Notification(notificationDate, type);
    }
}
