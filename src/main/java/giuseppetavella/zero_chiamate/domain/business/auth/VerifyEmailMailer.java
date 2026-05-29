package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.config.EmailTemplate;
import giuseppetavella.zero_chiamate.domain.business.auth.params.VerifyEmailEmailParams;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.ProblemsEmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.params.EmailTemplateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class VerifyEmailMailer {

    @Autowired
    private EmailService emailService;

    @Autowired
    private ProblemsEmailService problemsEmailService;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyEmailMailer.class);

    

    /**
     * Verify your email.
     * Should be sent only after signup or to verify a user's email.
     */
    @Async
    public void send(User user, String verificationUrl) throws EmailSendingException
    {
        
        var emailParams = new VerifyEmailEmailParams(
                user.getFirstname(), 
                verificationUrl
        );
        
        var subject = "Conferma la tua email";
        
        // we are running async, so must log/alert
        try {

            emailService.sendTemplate(new EmailTemplateParams(
                    EmailTemplate.VERIFY_EMAIL,
                    toTemplateVars(emailParams),
                    user.getEmail(),
                    subject
            ));

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
     * Generate the email params.
     *
     * @return
     */
    private Map<String, Object> toTemplateVars(VerifyEmailEmailParams params) {
        return Map.of(
                "firstname", params.firstname(),
                "verificationUrl", params.verificationUrl()
        );
    }



}
