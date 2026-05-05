package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.jobs.concrete_jobs.EmailEmployeesWhoseContractAboutToExpire_JobExecutor;
import giuseppetavella.demo_login_system.jobs.concrete_jobs.JobExecutor;
import giuseppetavella.demo_login_system.jobs.enums.JobExecutionState;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionGetNextItemException;
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
    private JobExecutionService jobExecutionService;
    
    // ******************
    // CONCRETE JOB EXECUTORS
    // ******************
    
    @Autowired
    private EmailEmployeesWhoseContractAboutToExpire_JobExecutor emailEmployeesWhoseContractAboutToExpire_JobExecutor;
    
    // add more job executors here...
    
    
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

        // ********************
        // PROCESS PENDING JOB EXECUTION (if any) 
        // ********************
        // assumption: at any point in time, there can 
        // only be at most 1 pending job execution,
        // and that can only be the most recent job execution
        
        

        JobExecutionItem<?> nextItem = executor.getNextItem();
        
        // if there's no next item to process, we stop the entire job
        while(nextItem != null) {

            // ********************
            // ADD NEW JOB EXECUTION WITH PENDING STATE
            // ********************
            
            // add a job execution to the DB, before 
            // processing this item
            JobExecution currJobExecution = this.jobExecutionService.addNewJobExecution(
                    jobName, 
                    nextItem.getItemId()
            );
            
            boolean processingWasSuccess = true;
            
            String messageIfProcessingFailed = null;
            
            // ********************
            // PROCESS ITEM
            // ********************
            
            try {
                
                executor.processItem(nextItem);
                
            } catch (RuntimeException ex) {

                processingWasSuccess = false;
                messageIfProcessingFailed = ex.getMessage();
            
            }

            // ********************
            // UPDATE JOB EXECUTION STATE (SUCCESS, FAILED)
            // ********************
            
            if(processingWasSuccess) {
    
                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        currJobExecution,
                        JobExecutionState.SUCCESS
                );
                
            } else {
                
                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        currJobExecution,
                        JobExecutionState.FAILED,
                        messageIfProcessingFailed
                );
                
            }
            

            // ********************
            // GET NEXT ITEM
            // ********************
            
            // get next item to process
            // if this item will be null,
            // we'll stop the job immediately at the next
            // loop iteration
            
            try {
            
                nextItem = executor.getNextItem();
            
            } catch (RuntimeException ex) {
                
                // we exit immediately because 
                // an error while getting the next item
                // could signal a logical error in the query,
                // which could be an internal error and
                // not a business-related error
                throw new JobExecutionGetNextItemException(jobName, ex.getMessage());
                
            }
                

        }
        

        LOGGER.info("JOB '" + jobName + "': finished executing job.");

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
            
            return this.emailEmployeesWhoseContractAboutToExpire_JobExecutor;

        }
        
        
        // add more jobs here...     


        // the given job was not mapped to an executor

        LOGGER.severe("JOB '"+jobName+"': this job was not found, is not mapped or is not recognized internally.");

        throw new JobException("Job '"+jobName +"' was not found, is not mapped or is not recognized internally.");
        

    }
    

    
}
