package giuseppetavella.demo_login_system.controllers;

import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.payloads.in_response.NotificationToSendDTO;
import giuseppetavella.demo_login_system.services.NotificationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    
    @Autowired
    private NotificationsService notificationsService;

    /**
     * Find my notifications.
     * 
     * @param currentUser
     * @param filterRead
     * @param notificationType
     * @param page
     * @param pageSize
     * @param sortBy
     * @return
     */
    @GetMapping
    public Page<NotificationToSendDTO> findMyNotifications(@AuthenticationPrincipal User currentUser,
                                                          @RequestParam(value = "filterRead", required = false) Boolean filterRead,
                                                          @RequestParam(value = "type", required = false) String notificationType,
                                                          @RequestParam(value = "page", defaultValue = "0") int page,
                                                          @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
                                                          @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy)
   {
        // sortBy must be one of these values
       StringHelper.requireInValues(
               sortBy, 
               List.of("createdAt", "type"), 
               "sortBy"
       );
       
       
        Page<Notification> notificationsPage = this.notificationsService.findNotificationsByUser(
                currentUser,
                page,
                pageSize,
                sortBy,
                filterRead,
                notificationType
        );
        
        return notificationsPage.map(notification -> new NotificationToSendDTO(notification));
        
        
    }


    /**
     * Read my notification.
     */
    @PatchMapping("/{notificationId}/read")
    public NotificationToSendDTO readMyNotification(@AuthenticationPrincipal User currentUser,
                                                    @PathVariable(name = "notificationId") String notificationIdAsStr) 
    {

        UUID notificationId = StringHelper.parseUUID(notificationIdAsStr);

        Notification savedNotification = this.notificationsService.readMyNotification(notificationId, currentUser);
                
        return new NotificationToSendDTO(savedNotification);
        
    }
    
    

}
