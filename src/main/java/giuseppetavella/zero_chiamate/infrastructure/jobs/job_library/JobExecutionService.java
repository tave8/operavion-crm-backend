package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library;

import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.enums.JobExecutionState;
import giuseppetavella.zero_chiamate.domain.business.jobs.JobName;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions.JobExecutionException;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public JobExecution addNewJobExecution(@NonNull JobName jobName,
                                           @NonNull UUID lastProcessedItemId) 
    {
        // instantiate a new job execution (not managed by ORM)
        var jobExecution = new JobExecution(jobName, lastProcessedItemId);
        // add job execution to DB, so when it's returned, it's managed by ORM
        return jobManagerRepository.save(jobExecution);
    }

    /**
     * Set the desired state for the given job execution, and finish it.
     * 
     */
    public JobExecution updateJobExecutionStateAndFinish(Long jobExecutionId, 
                                                         JobExecutionState desiredState,
                                                         @Nullable String messageToConcatenate) throws JobExecutionException
    {
        
        var jobExecution = getById(jobExecutionId);
        
        jobExecution.setStateAndFinish(desiredState, messageToConcatenate);

        return this.save(jobExecution);
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
    public JobExecution getById(Long jobExecutionId) throws NotFoundException
    {
        return this.jobManagerRepository
                .findById(jobExecutionId)
                .orElseThrow(() -> new NotFoundException(jobExecutionId, "JOB EXECUTION"));
    }
    
    

}
