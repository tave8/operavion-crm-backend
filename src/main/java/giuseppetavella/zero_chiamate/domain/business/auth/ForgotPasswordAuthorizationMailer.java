package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.config.Template;
import giuseppetavella.zero_chiamate.domain.business.auth.params.ForgotPasswordAuthorizationEmailParams;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.email.ProblemsEmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ForgotPasswordAuthorizationMailer {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthEmailVerificationService authEmailVerificationService;
    
    
    @Autowired
    private ProblemsEmailService problemsEmailService;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(ForgotPasswordAuthorizationMailer.class);
    
    

    /**
     * Send forgot password authorization email.
     */
    public void send(User user, String verificationUrl) throws EmailSendingException
    {

        var emailParams = new ForgotPasswordAuthorizationEmailParams(
                verificationUrl
        );
        
        var subject = "Reset your password";

        emailService.sendEmailFromTemplate(
                Template.EMAIL_FORGOT_PASSWORD_AUTHORIZATION,
                toTemplateVars(emailParams),
                user.getEmail(),
                subject
        );

    }



    /**
     * Generate the email params.
     *
     * @return
     */
    private Map<String, Object> toTemplateVars(ForgotPasswordAuthorizationEmailParams params) {
        return Map.of(
                "verificationUrl", params.verificationUrl()
        );
    }



}
