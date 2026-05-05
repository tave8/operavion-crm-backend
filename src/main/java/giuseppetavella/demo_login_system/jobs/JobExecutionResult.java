package giuseppetavella.demo_login_system.jobs;

public class JobExecutionResult<T> {
    
    private final JobExecutionItem jobExecutionItem;
    
    public JobExecutionResult(JobExecutionItem<?> jobExecutionItem) {
        this.jobExecutionItem = jobExecutionItem;
    }

    public JobExecutionItem<T> getJobExecutionItem() {
        return jobExecutionItem;
    }
}
