package giuseppetavella.demo_login_system.jobs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Cron Job Manager defines the HOW to run a specific job,
 * and WHO is responsible for that specific execution.
 */
@Service
public class JobManager {

    @Autowired
    private JobExecutor jobExecutor;
    
    // logger
    private static final Logger LOGGER = Logger.getLogger(JobManager.class.getName());

    /**
     * Entry point for executing a job.
     * Provides centralized error handling. 
     * 
     * @param task which job to execute.
     *             
     * @throws JobExecutionException if any error during the execution of 
     *  the given cron job task
     * @throws JobException if a generic error occurred
     */
    public void executeJob(JobName task) {
        
        LOGGER.info("CRON JOB: TASK '"+task+"': this task was called to be executed, executing task...");
        
        try {
            
            if(task.equals(JobName.SEND_EMAIL_TO_USERS_WHO_SIGNEDUP_TODAY)) {
                
                this.jobExecutor.sendEmailToUsersWhoSignedupToday();
                
                LOGGER.info("CRON JOB: TASK '"+task+"': finished executing task with no errors.");
                
                return;
            }
            
            // add here more tasks...     

            
        } catch(RuntimeException ex) {
            
            throw new JobExecutionException(task, ex.getMessage());
            
        }

        LOGGER.severe("CRON JOB: TASK '"+task+"': this task was not found / is not mapped / not recognized internally.");
        
        throw new JobException("Cron job task '"+task +"' is not mapped / not recognized.");
        
    }
    

    
}
