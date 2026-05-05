package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.jobs.concrete_jobs.EmailEmployeesWhoseContractAboutToExpire_JobExecutor;
import giuseppetavella.demo_login_system.jobs.concrete_jobs.JobExecutor;
import giuseppetavella.demo_login_system.jobs.enums.JobExecutionState;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionGetNextItemException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionGetNextIncompleteExecutionException;
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
    public void executeJob(JobName jobName, 
                           boolean processIncompleteExecutions, 
                           boolean processNextItems) 
    {
        
        // ********************
        // GET JOB-SPECIFIC JOB EXECUTOR
        // ********************
        
        JobExecutor<?> executor = this.getJobExecutor(jobName);

        // ********************
        // START JOB 
        // ********************
        
        LOGGER.info("JOB '" + jobName + "': this job was called to be executed, executing it...");
        
        if(processIncompleteExecutions) {
            
            LOGGER.info("JOB '" + jobName + "': started processing existing incomplete job executions, if any...");
            
            // ********************
            // PROCESS INCOMPLETE JOB EXECUTIONS
            // ********************
            
            int countProcessedIncompleteJobExecutions = this.processIncompleteJobExecutions(jobName, executor);
    
            LOGGER.info("JOB '" + jobName + "': finished processing "+countProcessedIncompleteJobExecutions+" incomplete job executions.");
        
        }

        if(processNextItems) {
            
            LOGGER.info("JOB '" + jobName + "': started processing next items, if any...");
    
            // ********************
            // PROCESS NEXT ITEMS
            // ********************
    
            int countProcessedNextItems = this.processNextItems(jobName, executor);
            
            LOGGER.info("JOB '" + jobName + "': finished processing "+countProcessedNextItems+" next items.");
            
        }

        LOGGER.info("JOB '" + jobName + "': finished executing job.");

    }
    
    public void executeJob(JobName jobName,
                           boolean processIncompleteExecutions) 
    {
        this.executeJob(jobName, processIncompleteExecutions, false);
    }

    public void executeJob(JobName jobName)
    {
        this.executeJob(jobName, false, false);
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
            // ADD NEW JOB EXECUTION WITH INCOMPLETE STATE
            // ********************

            // add a job execution to the DB, before 
            // processing this item
            JobExecution currentJobExecution = this.jobExecutionService.addNewJobExecution(
                    jobName,
                    nextItem.getItemId()
            );

            boolean processingWasSuccess = true;

            String messageIfProcessingFailed = null;

            // ********************
            // PROCESS ITEM
            // ********************

            try {

                executor.processItem(nextItem, currentJobExecution);

            } catch (RuntimeException ex) {

                processingWasSuccess = false;
                messageIfProcessingFailed = ex.getMessage();

            }

            // ********************
            // UPDATE JOB EXECUTION STATE (SUCCESS, FAILED)
            // ********************

            if(processingWasSuccess) {

                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        currentJobExecution,
                        JobExecutionState.SUCCESS
                );

            } else {

                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        currentJobExecution,
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
     * Process incomplete job executions.
     */
    private int processIncompleteJobExecutions(JobName jobName, JobExecutor<?> executor) 
    {
        
        int countIncompleteJobExecutions = 0;

        Optional<JobExecution> maybeNextIncompleteJobExecution = this.jobManagerRepository.getNextIncompleteJobExecution(jobName.name());
        
        // System.out.println("next item of incomplete job execution: " + itemOfNextIncompleteExecution);

        // as long as there are incomplete job executions
        while(maybeNextIncompleteJobExecution.isPresent()) {
            
            // this is the incomplete job execution,
            // which therefore also contains the item that 
            // is in a incomplete state
            JobExecution incompleteJobExecution = maybeNextIncompleteJobExecution.get();
            
            // this item was probably not processed or 
            // its processin was interrupted
            JobExecutionItem<?> nextItem = executor.getItemById(
                    // the last processed item ID is searched, to get the  
                    // business-logic specific item
                    incompleteJobExecution.getLastProcessedItemId()
            );
            
            // it's possible that the item that was stored 
            // in a incomplete job execution could not be there,
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
                
                executor.processItem(nextItem, incompleteJobExecution);

            } catch (RuntimeException ex) {

                processingWasSuccess = false;
                messageIfProcessingFailed = ex.getMessage();

            }

            // ********************
            // UPDATE JOB EXECUTION STATE (SUCCESS, FAILED)
            // ********************

            if(processingWasSuccess) {

                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        incompleteJobExecution,
                        JobExecutionState.SUCCESS
                );

            } else {

                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        incompleteJobExecution,
                        JobExecutionState.FAILED,
                        messageIfProcessingFailed
                );

            }
            
            // at this point, we know for sure that 
            // what was a incomplete job execution, has now been processed 
            // to either success or failed
            countIncompleteJobExecutions += 1;
            
            // ********************
            // GET NEXT INCOMPLETE JOB EXECUTION
            // ********************

            try {

                maybeNextIncompleteJobExecution = this.jobManagerRepository.getNextIncompleteJobExecution(jobName.name());

            } catch (RuntimeException ex) {
                
                throw new JobExecutionGetNextIncompleteExecutionException(jobName, ex.getMessage());

            }


        }
        
        return countIncompleteJobExecutions;

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
