package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.exceptions.InvalidDataException;
import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.exceptions.UnauthorizedException;
import giuseppetavella.demo_login_system.helpers.AuthorizationHelper;
import giuseppetavella.demo_login_system.helpers.StringHelper;
import giuseppetavella.demo_login_system.repositories.NotificationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class NotificationsService {
    
    @Autowired
    private NotificationsRepository notificationsRepository;

    /**
     * Find notifications of the given user.
     */
    public Page<Notification> findNotificationsByUser(User user, 
                                                      int page, 
                                                      int pageSize, 
                                                      String sortBy, 
                                                      Boolean filterRead, 
                                                      String notificationType) throws InvalidDataException
    {

        // sortBy must be one of these values
        StringHelper.requireInValues(
                sortBy,
                List.of("createdAt", "type"),
                "sortBy"
        );

        // / the size of each page (how many elements in each page)
        int finalSize = Math.min(10, pageSize);
        // the page number
        int finalPage = Math.max(0, page);
        // page is the function that will get translated to SQL,
        // that will in turn filter the result set
        Pageable pageable = PageRequest.of(finalPage, finalSize, Sort.by(sortBy));
        
        return this.notificationsRepository.findNotificationsByUser(
                user,
                filterRead,
                notificationType,
                pageable
        );
        
    }

    /**
     * Get a notification by ID.
     */
    public Notification findById(UUID id) {
        return this.notificationsRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "NOTIFICATION"));
    }

    /**
     * Get a notification by ID.
     */
    public Notification findById(String id) {
        return this.findById(
                StringHelper.parseUUID(id)
        );
    }
    
    /**
     * Save a notification
     */
    public Notification save(Notification notification) {
        return this.notificationsRepository.save(notification);
    }


    /**
     * Read my notification.
     * @return
     */
    public Notification readMyNotification(UUID notificationId, User owner) throws NotFoundException, 
                                                                                    UnauthorizedException
    {
        
        Notification notification = this.findById(notificationId);

        AuthorizationHelper.requireSameUser(owner, notification.getUser());
        
        notification.read();
        
        return this.save(notification);
    
    }
    
    
    

}
