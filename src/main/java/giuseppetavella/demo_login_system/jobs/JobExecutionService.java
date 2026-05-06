package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.exceptions.NotFoundException;
import giuseppetavella.demo_login_system.jobs.enums.JobExecutionState;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobException;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class JobExecutionService {
    
    @Autowired
    private JobManagerRepository jobManagerRepository;

    /**
     * Save a job execution.
     */
    public JobExecution save(JobExecution jobExecution) {
        return this.jobManagerRepository.save(jobExecution);
    }

    /**
     * Add a new job execution.
     */
    public JobExecution addNewJobExecution(@NotNull JobName jobName,
                                           @NotNull UUID lastProcessedItemId) 
    {
        // instantiate a new job execution (not managed by ORM)
        JobExecution jobExecution = new JobExecution(jobName, lastProcessedItemId);
        // add job execution to DB, so when it's returned, it's managed by ORM
        return this.jobManagerRepository.save(jobExecution);
    }

    /**
     * Set the desired state for the given job execution, and finish it.
     * 
     */
    public JobExecution updateJobExecutionStateAndFinish(JobExecution jobExecution, 
                                                         JobExecutionState desiredState,
                                                         @Nullable String messageToConcatenate) throws JobExecutionException
    {
        jobExecution.setStateAndFinish(desiredState, messageToConcatenate);

        return this.save(jobExecution);
    }
    

    public JobExecution updateJobExecutionStateAndFinish(JobExecution jobExecution,
                                                         JobExecutionState desiredState) throws JobExecutionException
    {
        return this.updateJobExecutionStateAndFinish(jobExecution, desiredState, null);
    }


    /**
     * Increment the retry count of the given job execution.
     */
    public JobExecution incrementRetryCount(JobExecution jobExecution) {
        jobExecution.incrementRetryCount();
        return this.save(jobExecution);
    }

    /**
     * Get a job execution by ID.
     */
    public JobExecution findById(Long jobExecutionId) throws NotFoundException
    {
        return this.jobManagerRepository
                .findById(jobExecutionId)
                .orElseThrow(() -> new NotFoundException(jobExecutionId, "JOB EXECUTION"));
    }
    
    

}
