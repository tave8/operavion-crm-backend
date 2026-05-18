package giuseppetavella.demo_login_system.job_library.exceptions;


import giuseppetavella.demo_login_system.jobs.JobName;

public class JobExecutionGetNextItemException extends JobExecutionException {
    
    public JobExecutionGetNextItemException(JobName jobName, String details) {
        super(jobName, "Specific error while getting the next item. DETAILS: " + details);
    }
    
    
}
