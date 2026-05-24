package giuseppetavella.zero_chiamate.api.controllers.auth;

import giuseppetavella.zero_chiamate.domain.business.auth.AuthEmailVerificationService;
import giuseppetavella.zero_chiamate.exceptions.EmailVerificationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/verify-email")
public class VerifyEmailController {

    @Autowired
    private AuthEmailVerificationService authEmailVerificationService;


    /**
     * Verify if the code is valid.
     * If yes, the email of the account associated with this code, 
     * will be marked as verified.
     *
     * Returns a simple html page saying that email is now verified.
     */
    @GetMapping("/{code}")
    public String verifyEmail(@PathVariable String code) {

        try {

            this.authEmailVerificationService.verifyEmailVerificationCode(code);

        } catch (EmailVerificationException ex) {
            return ex.getMessage();
        }


        return "Your email was verified. Thank you. You may close this page and login.";
    }


}
