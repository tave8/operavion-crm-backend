package giuseppetavella.demo_login_system.jobs;

public class JobExecutionException extends JobException {
    public JobExecutionException(JobName jobName, String details) {
        super("Error while executing job '" + jobName + "'. DETAILS: " + details);
    }
}
