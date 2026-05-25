package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")
public class BillingController {
    
    // public void billing() {
    //
    //     var company = user.getCompany();
    //
    //     // assume: this is the admin that has just signed up,
    //     // and verified their email. remember this functionality
    //     // is also used to verify emails of any user, not just admins
    //     if (company.getStripeSubscriptionStatus() == StripeAPISubscriptionStatus.INCOMPLETE) {
    //
    //         String checkoutUrl = stripeAPIService.createCheckoutSession(company.getStripeCustomerId());
    //
    //         return ResponseEntity.status(302).header("Location", checkoutUrl).build();
    //
    //     }
    //    
    // }
    
}
