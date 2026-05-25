package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.business.billing.dto.to_send.BillingPortalToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")
public class BillingController {
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private StripeAPIService stripeAPIService;
    
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

    /**
     * <h1>Get the URL for the billing portal (Stripe)</h1>
     * 
     * The billing portal allows the user to manage their EXISTING
     * subscription. 
     * 
     * We return a DTO with the portal URL in it.
     * The frontend will have to redirect the user to that url.
     * 
     * Everything else will be taken care of by Stripe.
     * 
     * @param currentUser
     * @return
     */
    @GetMapping("/portal")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public BillingPortalToSendDTO getBillingPortalUrl(@AuthenticationPrincipal User currentUser) 
    {
        
        var stripeCustomerId = currentUser.getCompany().getStripeCustomerId();
        
        // if this company does not have a Stripe customer ID
        if(stripeCustomerId == null) {
            throw new InvalidDataException("Before creating a Stripe Customer Portal Session, "
                                            +"the Stripe customer ID of the company of the logged in user, cannot be null. "
                                            +"This likely means that the company was not saved as a Stripe customer. "
                                            +"Company ID is '" + currentUser.getCompany().getId() + "'. ");
        }
        
        // create the customer portal url
        String portalUrl = stripeAPIService.createCustomerPortalSession(stripeCustomerId);
        
        return new BillingPortalToSendDTO(portalUrl);
    }
    
}
