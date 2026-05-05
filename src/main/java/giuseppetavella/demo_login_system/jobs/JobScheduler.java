package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.jobs.enums.JobName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Cron Job Starter defines WHEN to trigger which cron jobs.
 * Each method should contain only one method call.
 */
@Service
public class JobScheduler {
    
    @Autowired
    private JobManager jobManager;
    

    // this cron means every minute
    @Scheduled(cron = "0 * * * * *")
    public void sendMeInvoiceReport() {
        
        // this.appEmailService.sendMeInvoiceReport();

    }

    // this cron means every minute
    @Scheduled(cron = "0 * * * * *")
    public void emailEmployeesWithContractAboutToExpire() {
        
        // this.jobManager.executeJob(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE);

    }



}
