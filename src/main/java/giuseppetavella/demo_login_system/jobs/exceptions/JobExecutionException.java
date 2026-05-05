package giuseppetavella.demo_login_system.jobs.exceptions;

public class JobExecutionException extends JobException {
    public JobExecutionException(String jobName, String details) {
        super("Error while executing job '" + jobName + "'. DETAILS: " + details);
    }
}
