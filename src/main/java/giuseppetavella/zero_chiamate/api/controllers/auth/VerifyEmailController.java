package giuseppetavella.zero_chiamate.api.controllers.auth;

import giuseppetavella.zero_chiamate.config.AppEnvironment;
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
    private AppEnvironment appEnvironment;


    /**
     * Verify if the code is valid.
     * If yes, the email of the account associated with this code, 
     * will be marked as verified.
     *
     * Returns a simple html page saying that email is now verified.
     */
    @GetMapping("/{code}")
    public ResponseEntity<?> verifyEmail(@PathVariable String code) {

        try {
            User user = authEmailVerificationService.verifyEmailVerificationCode(code);

            var company = user.getCompany();
            
            // if (company.getStripeSubscriptionStatus() == StripeAPISubscriptionStatus.INCOMPLETE) {
            //     String checkoutUrl = stripeAPIService.createCheckoutSession(company.getStripeCustomerId());
            //     return ResponseEntity.status(302).header("Location", checkoutUrl).build();
            // }

            return ResponseEntity.status(302)
                    .header("Location", appEnvironment.buildFrontendUrl("/?emailVerificationSuccess=true"))
                    .build();

        } catch (EmailVerificationException ex) {
            return ResponseEntity.status(302)
                    .header("Location", appEnvironment.buildFrontendUrl("/?emailVerificationSuccess=false"))
                    .build();
        }
        
    }


}
