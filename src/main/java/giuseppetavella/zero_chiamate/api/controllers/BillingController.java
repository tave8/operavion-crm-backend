package giuseppetavella.zero_chiamate.api.controllers;

import giuseppetavella.zero_chiamate.domain.business.billing.dto.to_send.BillingCheckoutToSendDTO;
import giuseppetavella.zero_chiamate.domain.business.billing.dto.to_send.BillingPortalToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.users.User;
import giuseppetavella.zero_chiamate.domain.entities.users.UsersService;
import giuseppetavella.zero_chiamate.exceptions.BillingException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/billing")
public class BillingController {
    
    @Autowired
    private UsersService usersService;
    
    @Autowired
    private StripeAPIService stripeAPIService;


    /**
     * Create a subscription for the current user's company.
     * 
     * @param currentUser
     * @return
     */
    @PostMapping("/checkout")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public BillingCheckoutToSendDTO createCheckout(@AuthenticationPrincipal User currentUser)
    {

        var company = currentUser.getCompany();
        
        var stripeCustomerId = company.getStripeCustomerId();

        // if this company does not have a Stripe customer ID
        if(stripeCustomerId == null) {
            throw new InvalidDataException("Before creating a Stripe Checkout Session, "
                    +"the Stripe customer ID of the company of the logged in user, cannot be null. "
                    +"This likely means that the company was not saved as a Stripe customer. "
                    +"Company ID is '" + currentUser.getCompany().getId() + "'. ");
        }
        
        // company can create a subscription, only if no subscription exists
        if (company.isStripeSubscriptionIncomplete()) {

            String checkoutUrl = stripeAPIService.createCheckoutSession(company.getStripeCustomerId());
            
            return new BillingCheckoutToSendDTO(checkoutUrl);

        }
        
        throw new BillingException("Company with ID '" + company.getId() + "' cannot create a new Stripe subscription "
                                    +", because a Stripe subscription with ID '"+ company.getStripeCustomerId()
                                    +"' and status " + company.getStripeSubscriptionStatus() 
                                    +" already exists.");
            
    }
    


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
    @PostMapping("/portal")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public BillingPortalToSendDTO createBillingPortalUrl(@AuthenticationPrincipal User currentUser) 
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
