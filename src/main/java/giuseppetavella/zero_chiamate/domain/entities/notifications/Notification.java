package giuseppetavella.zero_chiamate.domain.entities.notifications;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.NotificationException;
import giuseppetavella.zero_chiamate.helpers.TimeHelper;
import giuseppetavella.zero_chiamate.infrastructure.notification.NotificationData;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "notifications")
public class Notification {
    
    @Id
    @GeneratedValue
    @Column(name = "notification_id")
    private UUID notificationId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String body;

    @Column(columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private NotificationData data;
    
    @Column(name = "read_at")
    private OffsetDateTime readAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
    
    protected Notification() {}
    
    public Notification(User user, 
                        NotificationType type, 
                        String title, 
                        String body, 
                        NotificationData notificationData, 
                        OffsetDateTime expiresAt) 
    {
        
        this.user = user;
        // we store the notification type as string
        this.type = type.name();
        this.title = title;
        this.body = body;
        this.data = notificationData;
        this.expiresAt = expiresAt;
        this.createdAt = OffsetDateTime.now();
        
    }
    
    
    public Notification(User user, 
                        NotificationType type,
                        String title, 
                        String body,
                        NotificationData notificationData) 
    {   
        this(user, type, title, body, notificationData, null);
    }


    public Notification(User user, 
                        NotificationType type,
                        String title, 
                        String body,
                        OffsetDateTime expiresAt) 
    {
        this(user, type, title, body, new NotificationData(), expiresAt);
    }

    public Notification(User user, 
                        NotificationType type,
                        String title, 
                        String body)
    {
        this(user, type, title, body, new NotificationData(), null);
    }

    public Notification(User user,
                        NotificationType type,
                        String title)
    {
        this(user, type, title, "", new NotificationData(), null);
    }


    public void setData(NotificationData data) {
        this.data = data;
    }
    

    /**
     * 
     * @param expiresAt
     */
    public void setExpiresAt(OffsetDateTime expiresAt) {
        
        // it's okay if the expiration time is null
        if(expiresAt == null) {
            this.expiresAt = null;
        }
        
        // the expiry date must be >= now (so in the future)
        
        if(!TimeHelper.isNowOrFuture(expiresAt)) {
            throw new NotificationException(
                    this,
                    "The expiresAt attribute of the notification must be now or in the future, "
                            +"got "  + expiresAt + " instead."
            );
        }
        
        this.expiresAt = expiresAt;
    }

    /**
     * Update the readAt attribute of the notification,
     * marking the time of when the notification was read.
     * 
     * Cannot set the notification as read again.
     * 
     */
    public void read() {
        
        boolean readAtWasSet = this.getReadAt() != null;
        
        if(readAtWasSet) {
            throw new NotificationException(
                    this, 
                    "Cannot read this notification again."
            );
        }
        
        this.readAt = OffsetDateTime.now();
    }


    public NotificationType getType() {

        try {

            return NotificationType.valueOf(type);

        } catch(IllegalArgumentException ex) {

            throw new NotificationException(
                    this,
                    "While parsing the notification type '" + type + "' into an enum constant, "
                            +"no matching enum constant was found. This means that "
                            +"this notification type was deleted or modified. "
                            +"This error should be handled better. "
            );

        }

    }
    

    public String getBody() {
        return body;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getExpiresAt() {
        return expiresAt;
    }

    public String getTitle() {
        return title;
    }

    public NotificationData getData() {
        return data;
    }

    public UUID getNotificationId() {
        return notificationId;
    }

    public OffsetDateTime getReadAt() {
        return readAt;
    }


    public User getUser() {
        return user;
    }
    

    @Override
    public String toString() {
        return "Notification{" +
                "body='" + body + '\'' +
                ", notificationId=" + notificationId +
                ", title='" + title + '\'' +
                ", type='" + type + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
