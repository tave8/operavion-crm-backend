package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions;


import giuseppetavella.zero_chiamate.infrastructure.jobs.jobs.JobName;

public class JobExecutionGetNextItemException extends JobExecutionException {
    
    public JobExecutionGetNextItemException(JobName jobName, String details) {
        super(jobName, "Specific error while getting the next item. DETAILS: " + details);
    }
    
    
}
