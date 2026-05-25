package giuseppetavella.zero_chiamate.api.controllers.auth;

import giuseppetavella.zero_chiamate.config.AppEnvironment;
import giuseppetavella.zero_chiamate.config.FrontendRoutes;
import giuseppetavella.zero_chiamate.domain.business.auth.AuthEmailVerificationService;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.exceptions.EmailVerificationException;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/verify-email")
public class VerifyEmailController {

    @Autowired
    private AuthEmailVerificationService authEmailVerificationService;
    
    @Autowired
    private StripeAPIService stripeAPIService;
    
    @Autowired
    private FrontendRoutes frontendRoutes;


    /**
     * Verify if the code is valid.
     * If yes, the email of the account associated with this code, 
     * will be marked as verified.
     *
     * Returns a simple html page saying that email is now verified.
     */
    @GetMapping("/{code}")
    public ResponseEntity<?> verifyEmail(@PathVariable String code) 
    {

        try {
            
            // verify email with code
            User user = authEmailVerificationService.verifyEmailVerificationCode(code);
            

            // code was valid: email verified
            return ResponseEntity.status(302)
                    .header("Location", frontendRoutes.emailVerificationSuccess())
                    .build();

        } catch (EmailVerificationException ex) {
        
            // code is not valid
            return ResponseEntity.status(302)
                    .header("Location", frontendRoutes.emailVerificationFailed())
                    .build();
        
        }
        
    }


}
