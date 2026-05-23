package giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.exceptions;

public class JobException extends RuntimeException {
    public JobException(String message) {
        super("Generic error while working with a job. DETAILS: " + message);
    }
}
