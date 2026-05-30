package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library;

import giuseppetavella.zero_chiamate.infrastructure.email.ProblemsEmailService;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.enums.JobExecutionState;
import giuseppetavella.zero_chiamate.domain.business.jobs.JobName;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions.JobExecutionGetNextIncompleteExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Job Manager for incomplete job executions.
 *
 * Owns the HOW to re-process job executions that were left
 * in an {@link JobExecutionState#INCOMPLETE} state.
 *
 * It is used by {@link JobManager} via composition.
 */
@Service
public class JobManagerForIncomplete {

    @Autowired
    private JobExecutionService jobExecutionService;

    @Autowired
    private JobManagerRepository repo;

    @Autowired
    private ProblemsEmailService problemsEmailService;


    /**
     * Process incomplete job executions.
     *
     * <h1>The loop</h1>
     *
     * The loop of <code>JobManagerForIncomplete.processIncompleteJobExecutions()</code> runs
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
     * Also, this method (<code>JobManagerForIncomplete.processIncompleteJobExecutions()</code>)
     * does not even call <code>JobExecutor.getNextItems()</code>.
     *
     * <h1>Recap</h1>
     *
     * One more time, to recap the difference:
     *
     * <ul>
     *     <li>The loop of this method, <code>JobManagerForIncomplete.processIncompleteJobExecutions()</code>, will
     *          stop only when all incomplete job executions of the given job will have been re-processed,
     *          and marked with a state different from incomplete</li>
     *     <li><code>JobManagerRepository.getNextIncompleteJobExecution()</code>
     *          gets the next among ALL incomplete executions of the given job</li>
     *     <li><code>JobExecutor.getItemByIdOnIncompleteExecution()</code> applies
     *          job-specific logic to retrieve the item in the current incomplete job execution</li>
     * </ul>
     *
     */
    public int processIncompleteJobExecutions(JobName jobName, JobExecutor<?> executor)
    {

        var countIncompleteJobExecutions = 0;

        // i get the first incomplete job execution, if any
        var maybeNextIncompleteJobExecution = repo.getNextIncompleteJobExecution(jobName.name());

        // as long as there are incomplete job executions
        while(maybeNextIncompleteJobExecution.isPresent()) {

            // this is the incomplete job execution,
            // which therefore also contains the item that
            // is in a incomplete state
            var incompleteJobExecution = maybeNextIncompleteJobExecution.get();

            // this item was probably not processed or
            // its processin was interrupted
            var nextItem = executor.getItemByIdOnIncompleteExecution(
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
            var isRetryCountExceeded = incompleteJobExecution.getRetryCount() >= executor.getMaxRetries();

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
                jobExecutionService.incrementRetryCount(incompleteJobExecution);

                try {

                    executor.doProcessItem(nextItem, incompleteJobExecution);

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

            updateJobExecutionState(
                    incompleteJobExecution.getId(),
                    isRetryCountExceeded,
                    errorDuringProcessing
            );

            // ********************
            // IF UNSUCCESSFUL JOB EXECUTION, SEND ALERT
            // ********************

            doIfUnsuccessful(
                    incompleteJobExecution.getId(),
                    isRetryCountExceeded,
                    errorDuringProcessing,
                    executor
            );


            // at this point, we know for sure that
            // what was a incomplete job execution, has now been processed
            // to either success or failed
            countIncompleteJobExecutions += 1;

            // ********************
            // GET NEXT INCOMPLETE JOB EXECUTION
            // ********************

            try {

                maybeNextIncompleteJobExecution = repo.getNextIncompleteJobExecution(jobName.name());

            } catch (RuntimeException ex) {

                throw new JobExecutionGetNextIncompleteExecutionException(jobName, ex.getMessage());

            }


        }

        return countIncompleteJobExecutions;

    }


    /**
     * Update the state of an incomplete job execution after a re-processing attempt.
     *
     * An incomplete job execution can end up in one of three states:
     * <ul>
     *     <li><code>ABANDONED</code> if its retry count was exceeded,
     *          so it was not re-processed at all</li>
     *     <li><code>SUCCESS</code> if it was re-processed without error</li>
     *     <li><code>FAILED</code> if it threw an error during re-processing</li>
     * </ul>
     */
    public void updateJobExecutionState(Long jobExecutionId,
                                        boolean isRetryCountExceeded,
                                        RuntimeException errorDuringProcessing)
    {
        // if this execution was abandoned for
        // having exceed max retries allowed
        if(isRetryCountExceeded) {

            jobExecutionService.updateJobExecutionStateAndFinish(
                    jobExecutionId,
                    JobExecutionState.ABANDONED,
                    "This job execution was abandoned because "
                                        +"its number of retries on incomplete state "
                                        +" exceeded its max number of retries."
            );

        }
        // if this execution did not throw error
        else if(errorDuringProcessing == null) {

            jobExecutionService.updateJobExecutionStateAndFinish(
                    jobExecutionId,
                    JobExecutionState.SUCCESS,
                    null
            );

        }
        // if this execution did throw error
        else {

            jobExecutionService.updateJobExecutionStateAndFinish(
                    jobExecutionId,
                    JobExecutionState.FAILED,
                    "Error during processing of incomplete job execution. "
                                        +"DETAILS: " + errorDuringProcessing.getMessage()
            );

        }
    }


    /**
     * Send an alert to the developer when the re-processing of an
     * incomplete job execution was unsuccessful, that is, when it
     * exceeded its max retries or when it threw an error.
     */
    public void doIfUnsuccessful(Long jobExecutionId,
                                 boolean isRetryCountExceeded,
                                 RuntimeException errorDuringProcessing,
                                 JobExecutor<?> executor)
    {
        // whether the job execution throw an error
        // or it exceeded retries, we send an email to developer
        if (isRetryCountExceeded || errorDuringProcessing != null) {

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