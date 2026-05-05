package giuseppetavella.demo_login_system.jobs.concrete_jobs;

import giuseppetavella.demo_login_system.jobs.JobExecutionItem;
import giuseppetavella.demo_login_system.jobs.enums.JobName;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public abstract class JobExecutor<T> {
    
    private final JobName jobName;
    
    public JobExecutor(JobName jobName) {
        this.jobName = jobName;
    }
    
    public abstract void processItem(@Nullable JobExecutionItem<?> itemToProcess);
    
    public abstract @Nullable JobExecutionItem<T> getNextItem();

    public JobName getJobName() {
        return jobName;
    }
}
