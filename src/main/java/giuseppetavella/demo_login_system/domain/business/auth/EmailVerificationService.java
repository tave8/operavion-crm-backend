package giuseppetavella.demo_login_system.domain.business.auth;

import giuseppetavella.demo_login_system.domain.entities.users.User;
import giuseppetavella.demo_login_system.exceptions.EmailVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailVerificationService {
    
    @Autowired
    private EmailVerificationRepository emailVerificationRepository;
    
    // a bean
    private final String serverUrl;

    // verification code time to live, in minutes
    private static final long VERIFICATION_CODE_TTL = 10;


    public EmailVerificationService(
            @Qualifier("serverUrl") String serverUrl)
    {
        this.serverUrl = serverUrl;
    }



    /**
     * Verify if the code is valid, not expired, and was not used.
     */
    @Transactional
    public void verifyEmailVerificationCode(String codeAsStr) throws EmailVerificationException
    {
        UUID code;

        try {

            code = UUID.fromString(codeAsStr);

        } catch(IllegalArgumentException ex) {
            // the code is not even a valid UUID
            throw new EmailVerificationException("This code is not valid (error 1)");
        }

        OffsetDateTime validSince = OffsetDateTime.now().minusMinutes(VERIFICATION_CODE_TTL);

        Optional<EmailVerificationCode> maybeCodeFromDB = this.emailVerificationRepository.getCode(code, validSince);

        // the code is expired or was used
        if(maybeCodeFromDB.isEmpty()) {
            throw new EmailVerificationException("This code is not valid (error 2)");
        }

        // the code is valid
        EmailVerificationCode codeFromDB = maybeCodeFromDB.get();

        User userFromDB = codeFromDB.getUser();

        // code was valid but user was deleted
        if(userFromDB == null) {
            throw new EmailVerificationException("This code is not valid (error 3).");
        }

        // the email was already verified
        if(userFromDB.isVerifiedEmail()) {
            throw new EmailVerificationException("This code is not valid (error 4).");
        }


        // user exists 

        // mark the code as used
        codeFromDB.markAsUsed();

        try {

            // mark the account that this code belongs to, with email verified

            // it is possible that the code was not used but 
            // this verification link was clicked after the user has already 
            // verified the code, so we must check it
            userFromDB.markAsVerifiedEmail();

        } catch(EmailVerificationException ex) {
            throw new EmailVerificationException("This code is not valid (error 3)");
        }


    }




    /**
     * Generate a new email verification URL.
     * This includes adding a new email verification code in DB,
     * associated to the given user.
     */
    public String generateNewEmailVerificationUrl(User user) {
        String code = this.addEmailVerificationCode(user).toString();
        // which domain should be in the verification url?
        // the domain from which the email was sent?
        // so the domain on which domain this server is running?
        return this.buildEmailVerificationUrl(code);
    }


    /**
     * Add an email verification code for the given user.
     */
    private UUID addEmailVerificationCode(User user) {
        EmailVerificationCode code = new EmailVerificationCode(user);
        EmailVerificationCode codeFromDB = this.emailVerificationRepository.save(code);
        return codeFromDB.getCode();
    }

    /**
     * Build the email verification url from the verification code.
     * This will be the clickable link shown in the email.
     * The endpoint at this URL will then be called to verify this code.
     */
    private String buildEmailVerificationUrl(String code) {
        String path = "/auth/verify-email/" + code;
        return this.serverUrl + path;
    }
    
}
