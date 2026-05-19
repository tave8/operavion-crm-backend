package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.job_library.JobExecutor;
import giuseppetavella.demo_login_system.jobs.email_expiring_contracts.EmailExpiringContracts_JobExecutor;
import giuseppetavella.demo_login_system.job_library.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.email_operator_tomorrow_shift.EmailOperatorTomorrowShift_JobExecutor;
import giuseppetavella.demo_login_system.jobs.notify_admin_because_operator_has_no_shift.NotifyAdminBecauseOperatorHasNoShift_JobExecutor;
import giuseppetavella.demo_login_system.jobs.send_admin_weekly_report.notify_admin_because_operator_has_no_shift.SendAdminWeeklyReport_JobExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;


/**
 * 
 * <h1>Mapping JOB NAME : JOB EXECUTOR</h1>
 * 
 * Simple helper class that maps JOB NAME (what to run) : JOB EXECUTOR (who runs it).
 * 
 * Decouples "what job to run" from "what executor must run it" 
 * and avoids touching the JobManager when adding or editing 
 * a job executor, so that the JobManager contains the pure
 * orchestration / managing logic, and not this mapping logic.
 * 
 * Thus, when adding or editing a job name and a job executor,
 * and you want to, for example, add a new job, simply update 
 * the mapping that you find in the method <code>getJobExecutor()</code>.
 * 
 */
@Component
public class JobExecutors {
    
    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(JobExecutors.class);

    // ******************
    // CONCRETE JOB EXECUTORS: START
    // ******************

    @Autowired
    private EmailExpiringContracts_JobExecutor emailExpiringContracts_jobExecutor;
    
    @Autowired
    private EmailOperatorTomorrowShift_JobExecutor emailOperatorTomorrowShift_jobExecutor;
    
    @Autowired
    private NotifyAdminBecauseOperatorHasNoShift_JobExecutor notifyAdminBecauseOperatorHasNoShift_jobExecutor;
    
    @Autowired
    private SendAdminWeeklyReport_JobExecutor sendAdminWeeklyReport_jobExecutor;

    // add more job executors here...

    // ******************
    // CONCRETE JOB EXECUTORS: END
    // ******************


    /**
     * Get the job executor for the given job.
     * If a job executor for this job was not mapped,
     * an exception will be thrown.
     *
     * When a new job is added, you must only update the
     * mapping between the job name (in this method)
     * and the actual job executor.
     *
     */
    public JobExecutor<?> getJobExecutor(JobName jobName) throws JobException
    {
    
        // mapping job name (what to run) : job executor (who runs it)
        
        Map<JobName, JobExecutor<?>> jobExecutorMap = Map.of(

                JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE, this.emailExpiringContracts_jobExecutor,
        
                JobName.EMAIL_OPERATOR_TOMORROW_SHIFT, this.emailOperatorTomorrowShift_jobExecutor,
                
                JobName.NOTIFY_ADMIN_BECAUSE_OPERATOR_HAS_NO_SHIFT, this.notifyAdminBecauseOperatorHasNoShift_jobExecutor,
                
                JobName.SEND_ADMIN_WEEKLY_REPORT, this.sendAdminWeeklyReport_jobExecutor

                // add another mapping job name : job executor here...     
        );

        // if a job executor exists for the given job name
        if(jobExecutorMap.containsKey(jobName)) {

            return jobExecutorMap.get(jobName);

        }

        // the given job was not mapped to an executor

        LOGGER.error("JOB '{}': this job was not found, is not mapped or is not recognized internally.", jobName);

        throw new JobException("Job '"+jobName +"' was not found, is not mapped or is not recognized internally.");
        
    }
    
}
