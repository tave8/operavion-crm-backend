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
     * @param jobName which job to execute.
     *             
     * @throws JobExecutionException if any error during the execution of 
     *  the given cron job task
     * @throws JobException if a generic error occurred
     */
    public void executeJob(JobName jobName) {
        
        LOGGER.info("JOB '\"+jobName+\"': this jobName was called to be executed, executing jobName...");
        
        try {
            
            if(jobName.equals(JobName.SEND_EMAIL_TO_USERS_WHO_SIGNEDUP_TODAY)) {
                
                this.jobExecutor.sendEmailToUsersWhoSignedupToday();
                
                LOGGER.info("JOB '\"+jobName+\"': finished executing jobName with no errors.");
                
                return;
            }
            
            // add here more jobNames...     

            
        } catch(RuntimeException ex) {
            
            throw new JobExecutionException(jobName.name(), ex.getMessage());
            
        }

        LOGGER.severe("JOB '\"+jobName+\"': this jobName was not found / is not mapped / not recognized internally.");
        
        throw new JobException("jobName '"+jobName +"' is not mapped / not recognized.");
        
    }
    

    
}
