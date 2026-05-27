package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.domain.business.Template;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.ProblemsEmailService;
import giuseppetavella.zero_chiamate.infrastructure.jobs.job_library.JobManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthEmailService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthEmailVerificationService authEmailVerificationService;
    
    
    @Autowired
    private ProblemsEmailService problemsEmailService;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthEmailService.class);
    

    /**
     * Verify your email.
     * Should be sent only after signup or to verify a user's email.
     */
    @Async
    public void sendVerifyEmail(User user, String verificationUrl) throws EmailSendingException
    {

        Map<String, Object> vars = Map.of(
                "firstname", user.getFirstname(),
                "verificationUrl", verificationUrl
        );
        
        // throw new RuntimeException("just throwing an exception to see if async exception handling works");

        // we are running async, so must log/alert
        try {

            emailService.sendEmailFromTemplate(
                    Template.EMAIL_VERIFY_EMAIL,
                    vars,
                    user.getEmail(),
                    "Conferma la tua email"
            );

        } catch (RuntimeException ex) {

            // log & alert

            LOGGER.error("Error while sending verification email to user. Email: '{}', Name: '{}', Lastname: '{}'. Error: {}",
                    user.getEmail(), user.getFirstname(), user.getLastname(), ex.getMessage());

            problemsEmailService.alertDev(
                    "Error while sending verification email to user",
                    "Email: '" + user.getEmail() + "', " +
                            "Name: '" + user.getFirstname() + "', " +
                            "Lastname: '" + user.getLastname() + "'.",
                    ex
            );

        }

    }
    


    /**
     * Send forgot password authorization email.
     */
    public void sendForgotPasswordAuthorization(User user, String verificationUrl) throws EmailSendingException
    {

        Map<String, Object> vars = Map.of(
                "verificationUrl", verificationUrl
        );

        emailService.sendEmailFromTemplate(
                Template.EMAIL_FORGOT_PASSWORD_AUTHORIZATION,
                vars,
                user.getEmail(),
                "Reset your password"
        );

    }

}
