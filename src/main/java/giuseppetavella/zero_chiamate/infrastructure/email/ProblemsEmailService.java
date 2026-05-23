package giuseppetavella.zero_chiamate.infrastructure.email;

import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobExecution;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
public class ProblemsEmailService {
    
    @Autowired
    private EmailService emailService;


    /**
     * Email the developer, about a problem.
     */
    public void sendEmailToDevForProblem(String subject,
                                         String details,
                                         Exception exception)
    {

        OffsetDateTime now = OffsetDateTime.now();

        Map<String, Object> vars = Map.of(
                "message", exception.getMessage(),
                "details", details,
                "timestamp", now,
                "stackTrace", ExceptionUtils.getStackTrace(exception)
        );

        emailService.sendEmailFromTemplate(
                "dev_emails/error",
                vars,
                "giuseppetavella8@gmail.com",
                subject
        );
    }


    /**
     * This email should be sent when a system problem
     * occurs during a background job.
     */
    public void sendEmailToDevForSystemProblemDuringBackgroundJob(String jobName,
                                                                  Exception exception)
    {

        String subject = "System error during background job. Job name: " + jobName;

        String details = "Job name: " + jobName;

        this.sendEmailToDevForProblem(subject, details, exception);
    }


    /*
     * This email should be sent when an unsuccessful
     * job execution occurs (not a system problem during background job).
     * */
    public void sendEmailToDevForUnsuccessfulBackgroundJobExecution(JobExecution jobExecution,
                                                                    Integer maxRetries,
                                                                    Exception exception)
    {
        String state = jobExecution.getState().name();
        String jobName = jobExecution.getJobName().name();
        Long executionId = jobExecution.getId();
        String reason = exception != null ? exception.getMessage() : jobExecution.getMessage();
        String stackTrace = exception != null ? ExceptionUtils.getStackTrace(exception) : null;

        String subject = "[" + state + "] Background Job: " + jobName + " | Execution ID: " + executionId;

        Map<String, Object> vars = new HashMap<>();

        vars.put("jobName",     jobName);
        vars.put("executionId", executionId);
        vars.put("state",       state);
        vars.put("startedAt",   jobExecution.getStartedAt());
        vars.put("finishedAt",  jobExecution.getFinishedAt());
        vars.put("retryCount",  jobExecution.getRetryCount());
        vars.put("maxRetries",  maxRetries);
        vars.put("itemId",      jobExecution.getLastProcessedItemId());
        vars.put("reason",      reason);
        vars.put("stackTrace",  stackTrace);

        emailService.sendEmailFromTemplate(
                "dev_emails/unsuccessful_background_job",
                vars,
                "giuseppetavella8@gmail.com",
                subject
        );
    }
    
}
