package giuseppetavella.demo_login_system.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Cron Job Starter defines WHEN to trigger which cron jobs.
 * Each method should contain only one method call.
 */
@Service
public class CronJobStarter {
    
    @Autowired
    private CronJobManager cronJobManager;
    

    // this cron means every minute
    @Scheduled(cron = "0 * * * * *")
    public void sendMeInvoiceReport() {

        
        // this.appEmailService.sendMeInvoiceReport();
        // // //
        // LOGGER.info("CRON JOB: send email: email sent");

    }

    // this cron means every minute
    @Scheduled(cron = "0 * * * * *")
    public void sendEmailToUsersWhoSignedupToday() {

        // job name
        
        // this.cronJobManager.executeJob(CronJobTask.SEND_EMAIL_TO_USERS_WHO_SIGNEDUP_TODAY);
        
        // this.appEmailService.sendMeInvoiceReport();
        // // //
        // LOGGER.info("CRON JOB: send email: email sent");

    }



}
