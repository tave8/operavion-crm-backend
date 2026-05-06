package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.jobs.concrete_jobs.email_expiring_contracts.EmailExpiringContracts_JobExecutor;
import giuseppetavella.demo_login_system.jobs.enums.JobExecutionState;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionGetNextItemException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionGetNextIncompleteExecutionException;
import giuseppetavella.demo_login_system.services.AppEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

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
    
    @Autowired
    private AppEmailService appEmailService;
    
    // ******************
    // CONCRETE JOB EXECUTORS: START
    // ******************
    
    @Autowired
    private EmailExpiringContracts_JobExecutor emailExpiringContracts_JobExecutor;
    
    // add more job executors here...

    // ******************
    // CONCRETE JOB EXECUTORS: END
    // ******************
    
    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(JobManager.class);

    
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
        
        try {
            
            // ********************
            // GET JOB-SPECIFIC JOB EXECUTOR
            // ********************
            
            JobExecutor<?> executor = this.getJobExecutor(jobName);
    
            // ********************
            // START JOB 
            // ********************
    
            LOGGER.info("JOB '{}': this job was called to be executed, executing it...", jobName);
    
            // LOGGER.info("JOB '{}': execution mode [processIncompleteExecutions={}, processNextItems={}]",
            //         jobName, processIncompleteExecutions, processNextItems);
            
            if(processIncompleteExecutions) {
    
                // LOGGER.info("JOB '{}': started processing existing incomplete job executions...", jobName);
                
                // ********************
                // PROCESS INCOMPLETE JOB EXECUTIONS
                // ********************
                
                int countProcessedIncompleteJobExecutions = this.processIncompleteJobExecutions(jobName, executor);
    
                // LOGGER.info("JOB '{}': finished processing {} incomplete job executions.", jobName, countProcessedIncompleteJobExecutions);
            
            }
    
            if(processNextItems) {
    
                // LOGGER.info("JOB '{}': started processing next items...", jobName);
        
                // ********************
                // PROCESS NEXT ITEMS
                // ********************
        
                int countProcessedNextItems = this.processNextItems(jobName, executor);
    
                // LOGGER.info("JOB '{}': finished processing {} next items.", jobName, countProcessedNextItems);
                
            }
    
            LOGGER.info("JOB '{}': finished executing job.", jobName);
            
        } catch (Exception ex) {
            
            // i get an email with details about problem
            this.appEmailService.sendEmailToDevForSystemProblemDuringBackgroundJob(
                    jobName.name(), ex
            );
            
        }
        

    }
    
    public void executeJob(JobName jobName,
                           boolean processIncompleteExecutions) 
    {
        this.executeJob(jobName, processIncompleteExecutions, true);
    }

    public void executeJob(JobName jobName)
    {
        this.executeJob(jobName, true, true);
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
            
            // holds the exception itself, 
            // if any was raised during processing
            RuntimeException errorDuringProcessing = null;

            // ********************
            // PROCESS ITEM
            // ********************

            try {

                executor.processItem(nextItem, currentJobExecution.getMetadata());

            } catch (RuntimeException ex) {
                
                errorDuringProcessing = ex;

            }

            // ********************
            // UPDATE JOB EXECUTION STATE (SUCCESS, FAILED)
            // ********************

            // no error during processing -> job execution
            // was successful
            if(errorDuringProcessing == null) {

                // update the job execution state
                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        currentJobExecution,
                        JobExecutionState.SUCCESS
                );

            } 
            // error during processing
            else {
                
                // TODO: because the job execution has thrown, 
                //  we assume we want to rollback any changes made, unless
                //  they were explicitly saved in the processItem concrete job method.
                //  the assumption is that, even if the process item has thrown,
                //  which means it failed, we still save everything to DB,
                //  but this could include impartial data? not sure, it might depend 
                //  from job to job. same situation for process incomplete job executions in JobManager
                
                // update the job execution state
                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        currentJobExecution,
                        JobExecutionState.FAILED,
                        "Error during processing of next item. "
                                            +"DETAILS: " + errorDuringProcessing.getMessage()
                );

            }


            // ********************
            // IF UNSUCCESSFUL JOB EXECUTION, SEND ALERT
            // ********************

            // if the job execution throw an error 
            // we send an email to developer
            if (errorDuringProcessing != null) {

                // i get an email when job execution is unsuccessful
                this.appEmailService.sendEmailToDevForUnsuccessfulBackgroundJobExecution(
                        currentJobExecution,
                        executor.getMaxRetries(),
                        errorDuringProcessing
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

        // i get the first incomplete job execution, if any
        Optional<JobExecution> maybeNextIncompleteJobExecution = this.jobManagerRepository.getNextIncompleteJobExecution(jobName.name());
        
        // as long as there are incomplete job executions
        while(maybeNextIncompleteJobExecution.isPresent()) {
            
            // this is the incomplete job execution,
            // which therefore also contains the item that 
            // is in a incomplete state
            JobExecution incompleteJobExecution = maybeNextIncompleteJobExecution.get();
            
            // this item was probably not processed or 
            // its processin was interrupted
            JobExecutionItem<?> nextItem = executor.getItemByIdOnIncompleteExecution(
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
            
            // if the number of times that this job execution was retried
            // is >= than the allowed number of retries set by this executor,
            // then we must not run this job execution and must mark it somehow 
            boolean isRetryCountExceeded = incompleteJobExecution.getRetryCount() >= executor.getMaxRetries();
            
            RuntimeException errorDuringProcessing = null;

            // process the item, only if the retry count is not exceeded
            if (!isRetryCountExceeded) {
                
                // ********************
                // PROCESS ITEM
                // ********************
                
                // we increment the retry count before
                // processing this incomplete job execution
                // it wouldn't make sense to do it after the processing,
                // because at that point the job execution state
                // will soon be changed and won't be incomplete anymore
                this.jobExecutionService.incrementRetryCount(incompleteJobExecution);
    
                try {
                    
                    executor.processItem(nextItem, incompleteJobExecution.getMetadata());
    
                } catch (RuntimeException ex) {
    
                    errorDuringProcessing = ex;
    
                }
                
            }
            

            // ********************
            // UPDATE JOB EXECUTION STATE 
            // ********************

            // if this execution was abandoned for 
            // having exceed max retries allowed
            if(isRetryCountExceeded) {
                
                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        incompleteJobExecution,
                        JobExecutionState.ABANDONED,
                        "This job execution was abandoned because "
                                            +"its number of retries on incomplete state "
                                            +"  exceeded its max number of retries."
                );
                
            }
            // if this execution did not throw error
            else if(errorDuringProcessing == null) {

                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        incompleteJobExecution,
                        JobExecutionState.SUCCESS
                );

            } 
            // if this execution did throw error
            else {

                this.jobExecutionService.updateJobExecutionStateAndFinish(
                        incompleteJobExecution,
                        JobExecutionState.FAILED,
                        "Error during processing of incomplete job execution. "
                                            +"DETAILS: " + errorDuringProcessing.getMessage()
                );
                
            }

            // ********************
            // IF UNSUCCESSFUL JOB EXECUTION, SEND ALERT
            // ********************
            
            
            // whether the job execution throw an error 
            // or it exceeded retries, we send an email to developer
            if (isRetryCountExceeded || errorDuringProcessing != null) {
                
                // i get an email when job execution is unsuccessful
                this.appEmailService.sendEmailToDevForUnsuccessfulBackgroundJobExecution(
                        incompleteJobExecution,
                        executor.getMaxRetries(),
                        errorDuringProcessing
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

        Map<JobName, JobExecutor<?>> jobExecutorMap = Map.of(
        
            JobName.EMAIL_EMPLOYEES_WITH_CONTRACT_ABOUT_TO_EXPIRE, this.emailExpiringContracts_JobExecutor
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
