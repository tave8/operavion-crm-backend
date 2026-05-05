package giuseppetavella.demo_login_system.jobs.exceptions;


import giuseppetavella.demo_login_system.jobs.enums.JobName;

public class JobExecutionGetNextPendingExecutionException extends JobExecutionException {
    
    public JobExecutionGetNextPendingExecutionException(JobName jobName, String details) {
        super(jobName, "Specific error while getting the next pending job execution. DETAILS: " + details);
    }
    
    
}
