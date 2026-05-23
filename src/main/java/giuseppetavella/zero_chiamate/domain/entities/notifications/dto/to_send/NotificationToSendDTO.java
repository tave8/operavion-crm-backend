package giuseppetavella.zero_chiamate.domain.entities.notifications.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.notifications.Notification;

import java.time.OffsetDateTime;
import java.util.UUID;

public class NotificationToSendDTO {
    
    private final UUID notificationId;
    private final String type;
    private final String title;
    private final String body;
    // private data?
    private final OffsetDateTime readAt;
    private final OffsetDateTime expiresAt;
    private final OffsetDateTime createdAt;
    
    public NotificationToSendDTO(Notification notification) {
        this.notificationId = notification.getNotificationId();
        this.type = notification.getType().name();
        this.title = notification.getTitle();
        this.body = notification.getBody();
        this.readAt = notification.getReadAt();
        this.expiresAt = notification.getExpiresAt();
        this.createdAt = notification.getCreatedAt();
    }

    public String getBody() {
        return body;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getTitle() {
        return title;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getReadAt() {
        return readAt;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public String getType() {
        return type;
    }
}
