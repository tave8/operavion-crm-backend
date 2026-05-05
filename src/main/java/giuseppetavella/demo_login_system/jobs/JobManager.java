package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.jobs.concrete_jobs.EmailEmployeesWhoseContractAboutToExpire;
import giuseppetavella.demo_login_system.jobs.concrete_jobs.JobExecutor;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

/**
 * Cron Job Manager defines the HOW to run a specific job,
 * and WHO is responsible for that specific execution.
 */
@Service
public class JobManager {
    
    @Autowired
    private JobExecutionService jobExecutionService;
    
    // ******************
    // CONCRETE JOB EXECUTORS
    // ******************
    
    @Autowired
    private EmailEmployeesWhoseContractAboutToExpire emailEmployeesWhoseContractAboutToExpire;
    
    
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

        // give me the job executor for this job
        JobExecutor<?> executor = this.getJobExecutorElseThrow(jobName);


        LOGGER.info("JOB '" + jobName + "': this job was called to be executed, executing it...");

        // execute any pending executions (so existing job executions), 
        // before moving on to execute next items
        
        // the executor keeps on executing items until there's no more
        // the executor should continue processing items even if there's an error

        JobExecutionItem<?> nextItem = executor.getNextItem();
        
        // keep processing items until there's none left
        while(true) {
            
            try {
                
                // if there's no next item to process, we stop the entire job
                if(nextItem == null) {
                    break;
                }

                // add a job execution to the DB, before 
                // processing this item
                this.jobExecutionService.addNewJobExecution(
                        jobName, 
                        nextItem.getItemId()
                );

                executor.processItem(nextItem);
                
                // update the job execution, because
                // it means that the job execution was successful
      
                // get next item to process
                // if this item will be null,
                // we'll stop the job immediately at the next
                // loop iteration
                nextItem = executor.getNextItem();


            } catch (RuntimeException ex) {

                // mark this job execution as failed

                throw new JobExecutionException(jobName.name(), ex.getMessage());

            }
            
        }
        

        LOGGER.info("JOB '" + jobName + "': finished executing job with no errors.");

    }


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
    
    private JobExecutor<?> getJobExecutorElseThrow(JobName jobName) throws JobException
    {

        if(jobName.equals(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE)) {
            
            return this.emailEmployeesWhoseContractAboutToExpire;

        }
        
        
        // add more jobs here...     


        // the given job was not mapped to an executor

        LOGGER.severe("JOB '"+jobName+"': this job was not found, is not mapped or is not recognized internally.");

        throw new JobException("Job '"+jobName +"' was not found, is not mapped or is not recognized internally.");
        

    }
    

    
}
