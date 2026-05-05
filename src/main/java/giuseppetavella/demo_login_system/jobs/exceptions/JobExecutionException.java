package giuseppetavella.demo_login_system.jobs.exceptions;

import giuseppetavella.demo_login_system.jobs.JobExecution;
import giuseppetavella.demo_login_system.jobs.enums.JobName;

public class JobExecutionException extends JobException {
    public JobExecutionException(String jobName, String details) {
        super("Error while executing job '" + jobName + "'. DETAILS: " + details);
    }
    
    public JobExecutionException(JobName jobName, String details) {
        this(jobName.name(), details);
    }
    
}
