package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.jobs.concrete_jobs.EmailEmployeesWhoseContractAboutToExpire_JobExecutor;
import giuseppetavella.demo_login_system.jobs.concrete_jobs.JobExecutor;
import giuseppetavella.demo_login_system.jobs.enums.JobExecutionState;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionGetNextItemException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionGetNextPendingExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

/**
 * Cron Job Manager defines the HOW to run a specific job,
 * and WHO is responsible for that specific execution.
 */
@Service
public class JobManager {
    
    @Autowired
    private JobExecutionService jobExecutionService;
    
    @Autowired
    private JobManagerRepository jobManagerRepository;
    
    // ******************
    // CONCRETE JOB EXECUTORS: START
    // ******************
    
    @Autowired
    private EmailEmployeesWhoseContractAboutToExpire_JobExecutor emailEmployeesWhoseContractAboutToExpire_JobExecutor;
    
    // add more job executors here...

    // ******************
    // CONCRETE JOB EXECUTORS: END
    // ******************
    
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
        
        // ********************
        // GET JOB-SPECIFIC JOB EXECUTOR
        // ********************
        
        JobExecutor<?> executor = this.getJobExecutor(jobName);
        
        LOGGER.info("JOB '" + jobName + "': this job was called to be executed, executing it...");

        LOGGER.info("JOB '" + jobName + "': started processing existing pending job executions, if any...");
        
        // ********************
        // PROCESS PENDING JOB EXECUTIONS
        // ********************
        
        int countProcessedPendingJobExecutions = this.processPendingJobExecutions(jobName, executor);

        LOGGER.info("JOB '" + jobName + "': finished processing "+countProcessedPendingJobExecutions+" pending job executions.");

        LOGGER.info("JOB '" + jobName + "': started processing next items, if any...");

        // ********************
        // PROCESS NEXT ITEMS
        // ********************

        int countProcessedNextItems = this.processNextItems(jobName, executor);
        
        LOGGER.info("JOB '" + jobName + "': finished processing "+countProcessedNextItems+" next items.");

        LOGGER.info("JOB '" + jobName + "': finished executing job.");

    }

    /**
     * Process the next items.
     * 
     * @param jobName
     * @param executor
     */
    private int processNextItems(JobName jobName, JobExecutor<?> executor) {
        
        int countNextItems = 0;
        
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

            // at this point, we know we've successfully processed
            // this many items
            countNextItems += 1;
            
            // ********************
            // GET NEXT ITEM
            // ********************


            try {

                // get next item to process
                // if this item will be null,
                // we'll stop the job immediately at the next
                // loop iteration
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
        
        return countNextItems;
        
    }

    
    /**
     * Process pending job executions.
     */
    private int processPendingJobExecutions(JobName jobName, JobExecutor<?> executor) 
    {
        
        int countPendingJobExecutions = 0;

        Optional<JobExecution> maybeNextPendingJobExecution = this.jobManagerRepository.getNextPendingJobExecution(jobName.name());
        
        // System.out.println("next item of pending job execution: " + itemOfNextPendingExecution);

        // as long as there are pending job executions
        while(maybeNextPendingJobExecution.isPresent()) {
            
            // this is the pending job execution,
            // which therefore also contains the item that 
            // is in a pending state
            JobExecution pendingJobExecution = maybeNextPendingJobExecution.get();
            
            // this item was probably not processed or 
            // its processin was interrupted
            JobExecutionItem<?> nextItem = executor.getItemById(
                    // the last processed item ID is searched, to get the  
                    // business-logic specific item
                    pendingJobExecution.getLastProcessedItemId()
            );
            
            // it's possible that the item that was stored 
            // in a pending job execution could not be there,
            // so this case could be handled, if you want to
            // if(nextItem == null) {
            //    
            // }
            
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
                        pendingJobExecution,
                        JobExecutionState.SUCCESS
                );

            } else {

                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        pendingJobExecution,
                        JobExecutionState.FAILED,
                        messageIfProcessingFailed
                );

            }
            
            // at this point, we know for sure that 
            // what was a pending job execution, has now been processed 
            // to either success or failed
            countPendingJobExecutions += 1;
            
            // ********************
            // GET NEXT PENDING JOB EXECUTION
            // ********************

            try {

                maybeNextPendingJobExecution = this.jobManagerRepository.getNextPendingJobExecution(jobName.name());

            } catch (RuntimeException ex) {
                
                throw new JobExecutionGetNextPendingExecutionException(jobName, ex.getMessage());

            }


        }
        
        return countPendingJobExecutions;

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
    
    private JobExecutor<?> getJobExecutor(JobName jobName) throws JobException
    {

        if(jobName.equals(JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE)) {
            
            return this.emailEmployeesWhoseContractAboutToExpire_JobExecutor;

        }
        
        
        // add more job executors here...     


        // the given job was not mapped to an executor

        LOGGER.severe("JOB '"+jobName+"': this job was not found, is not mapped or is not recognized internally.");

        throw new JobException("Job '"+jobName +"' was not found, is not mapped or is not recognized internally.");
        

    }
    

    
}
