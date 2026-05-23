package giuseppetavella.demo_login_system.infrastructure.jobs.job_library;

import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.enums.JobExecutionState;
import giuseppetavella.demo_login_system.infrastructure.jobs.jobs.JobName;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.exceptions.JobException;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.exceptions.JobExecutionException;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.exceptions.JobExecutionGetNextItemException;
import giuseppetavella.demo_login_system.infrastructure.jobs.job_library.exceptions.JobExecutionGetNextIncompleteExecutionException;
import giuseppetavella.demo_login_system.infrastructure.jobs.jobs.JobExecutors;
import giuseppetavella.demo_login_system.infrastructure.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Cron Job Manager defines the HOW to run a specific job,
 * and WHO is responsible for that specific execution.
 */
@Service
public class JobManager {
    
    // mapping job name : job executor
    @Autowired
    private JobExecutors jobExecutors;
    
    @Autowired
    private JobExecutionService jobExecutionService;
    
    @Autowired
    private JobManagerRepository jobManagerRepository;
    
    @Autowired
    private EmailService appEmailService;
    
    
    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(JobManager.class);

    
    /**
     * Entry point for executing a job.
     * Provides centralized error handling. 
     * 
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
            
            JobExecutor<?> executor = this.jobExecutors.getJobExecutor(jobName);
    
            // ********************
            // START JOB 
            // ********************
    
            LOGGER.info("JOB '{}': started executing job.", jobName);
    
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

            LOGGER.error("JOB '{}': system error executing job. Error details: {}", jobName, ex.getMessage());
            
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
     * <h1>The loop</h1>
     * 
     * The loop of this method runs until there are no more 
     * next items to process. The definition of "next item"
     * is job-dependant. 
     * 
     * To avoid unnecessary abstraction at this stage, 
     * we rely on the job executor's implementation 
     * of the <code>JobExecutor.getNextItem()</code> method, to correctly end
     * the <code>JobManager.processNextItems()</code> method's loop.
     * 
     * In short, it is <code>JobExecutor.getNextItem()</code>
     * responsability to correctly get the next item, and thus
     * avoid re-getting items that were already processed.
     * For example, a basic mechanism for proper functioning, is the following:
     *
     * <pre>
     *     
     *  In JobExecutor.getNextItem() implementation:    
     *     
     *     get any next item based on the logic of this job
     *     AND 
     *     this item was not processed by this job 
     *     
     * </pre>
     * 
     * How do we know that an item was processed by a job?
     * Well, we look at the job executions DB table, there's a field
     * called <code>last_item_processed_id</code> or something like that,
     * and get all processed items by filtering by job name.
     * 
     * Above was the pseudocode for getting a next (and also new) item that was 
     * never processed by this job, however we might have other needs 
     * and we may want to re-process the same item, for example:
     * 
     * <pre>
     *     
     *  In JobExecutor.getNextItem() implementation:
     *
     *     get any next item based on the logic of this job
     *     AND 
     *     this item was not processed TODAY by this job 
     *      
     * </pre>
     * 
     * With the example above, we've just implemented a system 
     * for re-processing the same items, only if they were not 
     * processed today. (For example we might want to alert employees
     * that their contract is expiring, and we want to do so every day 
     * until the contract is renewed).
     * 
     * In this example, we need to re-process not necessarily the same exact
     * employees (because between this execution and the next they might have changed)
     * but we will certainly have no guarantee that they will be unique employees.
     * In other words: In general, they won't be unique employees.
     * 
     * In conclusion, we can use the <code>last_processed_item_id</code> field
     * in the job executions DB table, to process unique items per job, 
     * or to also process in general non-unique items that fulfill other conditions. 
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
     * 
     * <h1>The loop</h1>
     * 
     * The loop of <code>JobManager.processIncompleteJobExecutions()</code> runs
     * as long as there are incomplete job executions, and 
     * stops at the first retrieval that returns null, when trying 
     * to get the next incomplete job execution. 
     * 
     * To avoid unnecessary complexity at this stage, 
     * given a job, ALL its incomplete executions will be re-processed.
     * This means that the retrieval logic for incomplete job executions
     * has nothing to do with the job-specific 
     * <code>JobExecutor.getItemByIdOnIncompleteExecution()</code>.
     * 
     * Also, this method (<code>JobManager.processIncompleteJobExecutions()</code>)
     * does not even call <code>JobExecutor.getNextItems()</code>. 
     * 
     * <h1>Recap</h1>
     * 
     * One more time, to recap the difference:
     * 
     * <ul>
     *     <li>The loop of this method, <code>JobManager.processIncompleteJobExecutions()</code>, will
     *          stop only when all incomplete job executions of the given job will have been re-processed,
     *          and marked with a state different from incomplete</li>
     *     <li><code>JobManagerRepository.getNextIncompleteJobExecution()</code> 
     *          gets the next among ALL incomplete executions of the given job</li>
     *     <li><code>JobExecutor.getItemByIdOnIncompleteExecution()</code> applies
     *          job-specific logic to retrieve the item in the current incomplete job execution</li>
     * </ul>
     * 
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
            
            // TODO: note that nextItem could be null, we might want to 
            //  add that to the message for this job execution? same thing 
            //  for the method "process next items"
            

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
                                            +" exceeded its max number of retries."
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
    

    
}
