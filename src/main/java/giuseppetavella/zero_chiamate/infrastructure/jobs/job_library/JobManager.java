package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library;

import giuseppetavella.zero_chiamate.infrastructure.email.ProblemsEmailService;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.enums.JobExecutionState;
import giuseppetavella.zero_chiamate.domain.business.jobs.JobName;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions.JobException;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions.JobExecutionException;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions.JobExecutionGetNextItemException;
import giuseppetavella.zero_chiamate.domain.business.jobs.JobExecutors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private JobManagerForIncomplete jobManagerForIncomplete;

    @Autowired
    private ProblemsEmailService problemsEmailService;


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

            var executor = jobExecutors.getJobExecutor(jobName);

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

                int countProcessedIncompleteJobExecutions = jobManagerForIncomplete.processIncompleteJobExecutions(jobName, executor);

                // LOGGER.info("JOB '{}': finished processing {} incomplete job executions.", jobName, countProcessedIncompleteJobExecutions);

            }

            if(processNextItems) {

                // LOGGER.info("JOB '{}': started processing next items...", jobName);

                // ********************
                // PROCESS NEXT ITEMS
                // ********************

                processNextItems(jobName, executor);

                // LOGGER.info("JOB '{}': finished processing {} next items.", jobName, countProcessedNextItems);

            }

            LOGGER.info("JOB '{}': finished executing job.", jobName);

        } catch (Exception ex) {

            LOGGER.error("JOB '{}': system error executing job. Error details: {}", jobName, ex.getMessage());

            // i get an email with details about problem
            problemsEmailService.sendEmailToDevForSystemProblemDuringBackgroundJob(
                    jobName.name(), ex
            );

        }


    }

    // TODO: refactor this booleans. should be clearer when
    //  process incomplete executions or not, not rely on parameter  order
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
    private void processNextItems(JobName jobName,
                                  JobExecutor<?> executor)
    {

        var nextItem = executor.getNextItem();

        // if there's no next item to process, we stop the entire job
        while(nextItem != null) {

            // ********************
            // ADD NEW JOB EXECUTION WITH INCOMPLETE STATE
            // ********************

            // add a job execution to the DB, before
            // processing this item
            var currentJobExecution = jobExecutionService.addNewJobExecution(
                    jobName,
                    nextItem.getItemId()
            );

            // holds the exception itself,
            // if any was raised during processing
            RuntimeException processingError = null;

            // ********************
            // PROCESS ITEM
            // ********************

            try {

                executor.doProcessItem(
                        nextItem,
                        currentJobExecution
                );

            }
            // processing error
            catch (RuntimeException err) {

                processingError = err;

            }

            // update job execution
            updateJobExecutionState(
                    currentJobExecution.getId(),
                    processingError
            );

            // do if error during processing
            doIfErrorDuringProcessing(
                    currentJobExecution.getId(),
                    processingError,
                    executor
            );


            // ********************
            // GET NEXT ITEM
            // ********************


            try {

                // get next item to process
                // if item  null,
                // we'll stop the job immediately
                // at the next loop pass
                nextItem = executor.getNextItem();

            }
            // system error
            catch (RuntimeException err) {

                // exit immediately: error while getting next item
                // is system error, not domain error
                throw new JobExecutionGetNextItemException(jobName, err.getMessage());

            }


        }


    }


    /**
     * Update job execution state (success, failed etc.).
     *
     * Why re-fetch the job execution when we could pass it directly.
     * Because processing the current job execution might have thrown error,
     * and that has made the transaction rollback.
     * Thus, if we were to pass the in-memory current job execution here,
     * it would be like saying
     * "error during processing
     * -> rollback transaction
     * -> save the in-memory current job execution"
     * which would flush the in-memory job execution as is, which means,
     * it's as if we never rolled back.
     *
     *
     */
    public void updateJobExecutionState(Long jobExecutionId,
                                        RuntimeException processingError)
    {

        // no error during processing -> job execution
        // was successful
        if(processingError == null) {

            // update the job execution state
            jobExecutionService.updateJobExecutionStateAndFinish(
                    jobExecutionId,
                    JobExecutionState.SUCCESS,
                    null
            );

        }
        // error during processing
        else {

            // update the job execution state
            jobExecutionService.updateJobExecutionStateAndFinish(
                    jobExecutionId,
                    JobExecutionState.FAILED,
                    "Error during processing of next item. "
                            +"DETAILS: " + processingError.getMessage()
            );

        }
    }


    public void doIfErrorDuringProcessing(Long jobExecutionId,
                                          RuntimeException errorDuringProcessing,
                                          JobExecutor<?> executor)
    {
        // ********************
        // IF UNSUCCESSFUL JOB EXECUTION, SEND ALERT
        // ********************

        // if the job execution throw an error
        // we send an email to developer
        if (errorDuringProcessing != null) {

            var currentJobExecution = jobExecutionService.getById(jobExecutionId);

            // i get an email when job execution is unsuccessful
            problemsEmailService.sendEmailToDevForUnsuccessfulBackgroundJobExecution(
                    currentJobExecution,
                    executor.getMaxRetries(),
                    errorDuringProcessing
            );

        }
    }


}