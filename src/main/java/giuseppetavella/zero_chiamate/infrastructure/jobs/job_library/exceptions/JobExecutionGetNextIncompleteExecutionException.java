package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions;


import giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.JobName;

public class JobExecutionGetNextIncompleteExecutionException extends JobExecutionException {
    
    public JobExecutionGetNextIncompleteExecutionException(JobName jobName, String details) {
        super(jobName, "Specific error while getting the next incomplete job execution. DETAILS: " + details);
    }
    
    
}
