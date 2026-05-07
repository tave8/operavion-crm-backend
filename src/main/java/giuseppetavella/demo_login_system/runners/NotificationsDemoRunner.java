package giuseppetavella.demo_login_system.runners;

import giuseppetavella.demo_login_system.entities.Notification;
import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.enums.NotificationType;
import giuseppetavella.demo_login_system.services.NotificationsService;
import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class NotificationsDemoRunner implements CommandLineRunner {

    @Autowired
    private NotificationsService notificationsService;
    
    @Autowired
    private UsersService usersService;

    @Override
    public void run(String... args) throws Exception {

        // User userFromDB = this.usersService.findById("b16b6831-6c94-4f16-bf5f-89d1a60a5e79");
        // Notification notificationFromDB = this.notificationsService.findById("92970e6d-a8b2-42d7-b845-57a2b238eeaf");
        //
        // Notification notification = new Notification(
        //         userFromDB,
        //         NotificationType.EXPIRING_EMPLOYEE_CONTRACT,
        //         "this employee's contract is expiring",
        //         "take action!"
        // );

        // System.out.println(notificationFromDB);
        
        // this.notificationsService.save(notification);
        
    }
    
}
