package giuseppetavella.zero_chiamate.runners;

import giuseppetavella.zero_chiamate.domain.entities.notifications.NotificationsService;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
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

        // User userFromDB = this.usersService.findById("5f29cb26-7f20-417a-8445-cfa46b2ff783");
        // Notification notificationFromDB = this.notificationsService.findById("92970e6d-a8b2-42d7-b845-57a2b238eeaf");
        //

        // System.out.println(userFromDB);
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
