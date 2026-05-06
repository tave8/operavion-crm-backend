package giuseppetavella.demo_login_system.exceptions;

import giuseppetavella.demo_login_system.entities.Notification;

public class NotificationException extends RuntimeException {
    public NotificationException(String details) {
        super("There was an error while working with a notification. DETAILS: " + details);
    }
    
    public NotificationException(Notification notification, String details) {
        super("Error while working with notification with ID '" + notification.getNotificationId() + "'. DETAILS: " + details);
    }
    
}
