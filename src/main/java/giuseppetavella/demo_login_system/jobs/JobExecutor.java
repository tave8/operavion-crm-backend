package giuseppetavella.demo_login_system.jobs;

import giuseppetavella.demo_login_system.jobs.enums.JobName;
import giuseppetavella.demo_login_system.jobs.exceptions.JobExecutionException;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * This class defines the structure of a Job Executor.
 * 
 * A concrete Job Executor defines the business-specific logic
 * of what <code>getNextItem</code>, <code>processItem</code>,
 * <code>getItemById</code> mean.
 *
 * 
 * @param <T>
 */
@Service
public abstract class JobExecutor<T> {
    
    protected final JobName jobName;

    /**
     * How many times should the job execution be retried,
     * when the job execution is incomplete.
     * Default to 1, it means that a job execution that was
     * left with an incomplete state, will be re-processed
     * only one time and then its state will be marked as abandoned.
     */
    protected Integer maxRetries;

    /**
     * 
     * @param jobName
     * @param maxRetries the number of times the job executions of the subclass  
     *                   will be retried, if their state is incomplete. Must be >= 1 and <= 10.
     *                   This number therefore applies to all job executions of 
     *                   this <code>jobName</code>
     */
    public JobExecutor(JobName jobName, Integer maxRetries) {
        
        // max retries must be >= 1
        if (maxRetries == null || maxRetries < 1) {
            throw new JobExecutionException(
                    jobName,
                    "While instantiating JobExecutor, maxRetries value is too small or null. "
                            +"Must be >= 1. Got " + maxRetries + " instead."
            );
        }
        
        // max retries is too big
        if(maxRetries > 10) {
            throw new JobExecutionException(
                    jobName,
                    "While instantiating JobExecutor, maxRetries value is too big. "
                            +"Must be <= 10. Got " + maxRetries + " instead."
            );
        }
        
        this.jobName = jobName;
        this.maxRetries = maxRetries;
    }
    
    public JobExecutor(JobName jobName) {
        this(jobName, 1); 
    }

    /**
     * Process the given item with business-specific logic.
     */
    public abstract void processItem(JobExecutionItem<?> itemToProcess, JobExecutionMetadata jobExecutionMetadata);

    /**
     * Get the next item with business-specific logic.
     */
    public abstract JobExecutionItem<T> getNextItem();

    /**
     * Get the item, with business-specific logic.
     */
    public abstract JobExecutionItem<T> getItemById(UUID itemId);

    
    public JobName getJobName() {
        return jobName;
    }

    public Integer getMaxRetries() {
        return maxRetries;
    }
    
}
