package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Cron Job Executor defines WHAT to run.
 * 
 * It contains the business code that maps directly
 * to the core logic to be executed, for example:
 * - send emails
 * - generate a report and email me
 * - etc.
 */
@Service
public class JobExecutor {

    @Autowired
    private UsersService usersService;
    
    public void sendEmailToUsersWhoSignedupToday() {

        // process one user at a time 
        // start from the last user that was processed by this task,
        // whose task state is not complete     
        // this.usersService.fin

    }
    
}
