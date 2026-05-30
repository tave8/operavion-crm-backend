package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions;

import giuseppetavella.zero_chiamate.domain.business.jobs.JobName;

public class JobExecutionException extends JobException {
    public JobExecutionException(String jobName, String details) {
        super("Error while executing job '" + jobName + "'. DETAILS: " + details);
    }
    
    public JobExecutionException(JobName jobName, String details) {
        this(jobName.name(), details);
    }
    
}
