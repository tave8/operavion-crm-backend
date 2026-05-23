package giuseppetavella.zero_chiamate.exceptions;

import giuseppetavella.zero_chiamate.domain.entities.notifications.Notification;

public class NotificationException extends RuntimeException {
    public NotificationException(String details) {
        super("There was an error while working with a notification. DETAILS: " + details);
    }
    
    public NotificationException(Notification notification, String details) {
        super("Error while working with notification with ID '" + notification.getNotificationId() + "'. DETAILS: " + details);
    }
    
}
