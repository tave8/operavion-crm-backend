package giuseppetavella.demo_login_system.jobs;

public class CronJobException extends RuntimeException {
    public CronJobException(String message) {
        super("Generic error while working with a cron job task. DETAILS: " + message);
    }
}
