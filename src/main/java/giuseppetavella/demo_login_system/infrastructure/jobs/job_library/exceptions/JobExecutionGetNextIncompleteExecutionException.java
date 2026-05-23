package giuseppetavella.demo_login_system.infrastructure.jobs.job_library.exceptions;


import giuseppetavella.demo_login_system.infrastructure.jobs.jobs.JobName;

public class JobExecutionGetNextIncompleteExecutionException extends JobExecutionException {
    
    public JobExecutionGetNextIncompleteExecutionException(JobName jobName, String details) {
        super(jobName, "Specific error while getting the next incomplete job execution. DETAILS: " + details);
    }
    
    
}
