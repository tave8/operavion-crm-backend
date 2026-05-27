package giuseppetavella.zero_chiamate.infrastructure.jobs.jobs;

import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.CronSchedule;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobManager;
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
    

    @Scheduled(cron = CronSchedule.EVERY_HOUR)
    public void sendMeInvoiceReport() {
        
        // this.appEmailService.sendMeInvoiceReport();

    }

    @Scheduled(cron = CronSchedule.EVERY_HOUR)
    public void emailEmployeesWithContractAboutToExpire() {
        
        // this.jobManager.executeJob(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE);

    }

    @Scheduled(cron = CronSchedule.EVERY_HOUR)
    public void emailOperatorTomorrowShift() {
        
        this.jobManager.executeJob(JobName.EMAIL_OPERATOR_TOMORROW_SHIFT);
        
    }


    @Scheduled(cron = CronSchedule.EVERY_HOUR)
    public void notifyAdminBecauseOperatorHasNoShift() {

        this.jobManager.executeJob(JobName.NOTIFY_ADMIN_BECAUSE_OPERATOR_HAS_NO_SHIFT);

    }

    @Scheduled(cron = CronSchedule.EVERY_HOUR)
    public void sendAdminWeeklyReport() {

        // this.jobManager.executeJob(JobName.SEND_ADMIN_WEEKLY_REPORT);
        
        // this.jobManager.executeJob(JobName.NOTIFY_ADMIN_BECAUSE_OPERATOR_HAS_NO_SHIFT);

    }


    @Scheduled(cron = CronSchedule.EVERY_HOUR)
    public void sendAdminDiscrepancies() {

        // System.out.println("JOB SEND ADMIN DISCREPANCIES WAS CALLED");
        
        this.jobManager.executeJob(JobName.SEND_ADMIN_DISCREPANCIES);


    }




}
