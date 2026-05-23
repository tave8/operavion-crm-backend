package giuseppetavella.demo_login_system.infrastructure.notification;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 */
public class NotificationData {
    
    private final Map<String, Object> extra = new HashMap<>();
    
    public NotificationData() {
        
    }

    public Map<String, Object> getExtra() {
        return extra;
    }
}
