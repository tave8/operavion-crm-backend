package giuseppetavella.demo_login_system.jobs.exceptions;


import giuseppetavella.demo_login_system.jobs.enums.JobName;

public class JobExecutionGetNextIncompleteExecutionException extends JobExecutionException {
    
    public JobExecutionGetNextIncompleteExecutionException(JobName jobName, String details) {
        super(jobName, "Specific error while getting the next incomplete job execution. DETAILS: " + details);
    }
    
    
}
