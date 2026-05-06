package giuseppetavella.demo_login_system.services;

import giuseppetavella.demo_login_system.entities.User;
import giuseppetavella.demo_login_system.exceptions.EmailSendingException;
import giuseppetavella.demo_login_system.jobs.JobExecution;
import giuseppetavella.demo_login_system.models.EmailAttachment;
import giuseppetavella.demo_login_system.models.EmailAttachmentFromURL;
import giuseppetavella.demo_login_system.services.base.EmailService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Send business-specific emails.
 * 
 * Examples:
 * - welcome on signup
 * - reset password
 * - etc.
 */
@Service
public class AppEmailService extends EmailService {
 
    
    @Autowired
    private AppPdfService appPdfService;

    private final String serverUrl;

    public AppEmailService(@Qualifier("serverUrl") String serverUrl) {
        this.serverUrl = serverUrl;
    }


    /**
     * Send welcome email on signup.
     */
    // public void sendWelcome() {
    //     Context context = new Context();
    //     context.setVariable("firstname", "Giuseppe");
    //
    //     String htmlBody = templateEngine.process("emails/signup", context);
    //    
    //     this.sendEmail("giuseppetavella8@gmail.com", "Welcome!", htmlBody);
    // }

    /**
     * Send verify your account email.
     * Should be sent only after signup.
     */
    public void sendVerifyEmail(User user, String verificationUrl) throws EmailSendingException
    {
        
        Map<String, Object> vars = Map.of(
            "firstname", user.getFirstname(),
            "verificationUrl", verificationUrl
        );
        
        this.sendEmailFromTemplate(
                "emails/verify_email",
                vars,
                user.getEmail(),
                "Verify your email"
        );
        
    }


    /**
     * Send forgot password authorization email.
     */
    public void sendForgotPasswordAuthorization(User user, String verificationUrl) throws EmailSendingException
    {

        Map<String, Object> vars = Map.of(
            "verificationUrl", verificationUrl
        );

        this.sendEmailFromTemplate(
            "emails/forgot_password_authorization",
            vars,
            user.getEmail(),
            "Reset your password"
        );
        
    }


    /**
     * Send articles report email.
     */
    // public void sendArticlesReport(User user, byte[] articlesReportCsv) throws EmailSendingException 
    // {
    //
    //     Context context = new Context();
    //     context.setVariable("firstname", user.getFirstname());
    //
    //     String htmlBody = templateEngine.process("emails/articles_report", context);
    //    
    //     EmailAttachment attachment = new EmailAttachment(
    //             articlesReportCsv, 
    //             "articles_report.csv"
    //     );
    //
    //     this.sendEmail(
    //             user.getEmail(), 
    //             "Articles Report", 
    //             htmlBody, 
    //             attachment
    //     );
    // }
    

    public void sendPdf(String recipient, String pdfUrl)
    {
        this.sendEmail(
                recipient,
                "Here's your pdf",
                "Hello",
                new EmailAttachmentFromURL(pdfUrl, "pdf_from_internet.pdf")
        );
    }
    
    public void sendMeInvoiceReport() {
        
        // *****************
        // BUILD THE PDF
        // *****************
        
        // build the hashmap that gets passed to the html template
        // that will be turned into pdf
        Map<String, Object> pdfVars = Map.of();
        
        // generate email attachment from pdf
        EmailAttachment attachment = new EmailAttachment(
                this.appPdfService.generateInvoice(pdfVars).toAttachment(),
                "invoice_report.pdf"
        );
        
        // *****************
        // BUILD THE EMAIL
        // **************

        // build the hashmap that gets passed to the html template
        // that will be sent as email
        Map<String, Object> vars = Map.of(
                "firstname", "Giuseppe",
                "timeSent", OffsetDateTime.now()
        );
        
        this.sendEmailFromTemplate(
                "emails/invoice_report", 
                vars,
                "giuseppetavella8@gmail.com",
                "Your Invoice Report",
                attachment
        );
        
    }

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

        this.sendEmailFromTemplate(
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

        this.sendEmailFromTemplate(
                "dev_emails/unsuccessful_background_job",
                vars,
                "giuseppetavella8@gmail.com",
                subject
        );
    }
    

}
