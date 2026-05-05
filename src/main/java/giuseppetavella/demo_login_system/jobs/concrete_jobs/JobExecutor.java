package giuseppetavella.demo_login_system.jobs.concrete_jobs;

import giuseppetavella.demo_login_system.jobs.JobExecution;
import giuseppetavella.demo_login_system.jobs.JobExecutionItem;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import jakarta.annotation.Nullable;
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
    
    private final JobName jobName;
    
    public JobExecutor(JobName jobName) {
        this.jobName = jobName;
    }

    /**
     * Process the given item with business-specific logic.
     */
    public abstract void processItem(JobExecutionItem<?> itemToProcess, JobExecution currentJobExecution);

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
}
