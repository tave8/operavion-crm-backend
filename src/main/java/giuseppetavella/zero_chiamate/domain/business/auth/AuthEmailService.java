package giuseppetavella.zero_chiamate.domain.business.auth;

import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailSendingException;
import giuseppetavella.zero_chiamate.infrastructure.email.EmailService;
import giuseppetavella.zero_chiamate.infrastructure.pdf.AppPdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthEmailService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthEmailVerificationService authEmailVerificationService;

    @Autowired
    private AppPdfService appPdfService;
    
    

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

        emailService.sendEmailFromTemplate(
                "emails/verify_email",
                vars,
                user.getEmail(),
                "Conferma la tua email"
        );

    }

    /**
     * Generate a new code verification email code 
     * and send an email with that.
     */
    public void sendVerifyEmailWithVerificationUrl(User user) throws EmailSendingException
    {
        String verificationUrl = this.authEmailVerificationService.generateNewEmailVerificationUrl(user);

        this.sendVerifyEmail(user, verificationUrl);
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
                "emails/forgot_password_authorization",
                vars,
                user.getEmail(),
                "Reset your password"
        );

    }

}
