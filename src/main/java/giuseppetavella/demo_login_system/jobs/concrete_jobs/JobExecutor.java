package giuseppetavella.demo_login_system.jobs.concrete_jobs;

import giuseppetavella.demo_login_system.jobs.JobExecutionItem;
import giuseppetavella.demo_login_system.jobs.JobExecutionResult;
import giuseppetavella.demo_login_system.jobs.JobExecutorRepository;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class JobExecutor<T> {
    
    // dependency injection by subclass
    // subclass itself must be injected and not manually instantiated
    protected final JobExecutorRepository jobExecutorRepository;
    
    private final JobName jobName;
    
    public JobExecutor(JobName jobName, JobExecutorRepository jobExecutorRepository) {
        this.jobName = jobName;
        this.jobExecutorRepository = jobExecutorRepository;
    }
    
    public abstract void processItem(@Nullable JobExecutionItem<?> itemToProcess);
    
    public abstract @Nullable JobExecutionItem<T> getNextItem();

    public JobName getJobName() {
        return jobName;
    }
}
