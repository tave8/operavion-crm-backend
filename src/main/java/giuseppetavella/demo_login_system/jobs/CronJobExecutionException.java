package giuseppetavella.demo_login_system.jobs;

public class CronJobExecutionException extends CronJobException {
    public CronJobExecutionException(CronJobTask task, String details) {
        super("Error while executing cron job task '" + task + "'. DETAILS: " + details);
    }
}
