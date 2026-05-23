package giuseppetavella.demo_login_system.infrastructure.jobs.job_library.exceptions;

public class JobException extends RuntimeException {
    public JobException(String message) {
        super("Generic error while working with a job. DETAILS: " + message);
    }
}
