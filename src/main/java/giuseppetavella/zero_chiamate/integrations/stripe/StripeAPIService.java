package giuseppetavella.zero_chiamate.integrations.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import giuseppetavella.zero_chiamate.config.FrontendRoutes;
import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataException;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.exceptions.integrations.stripe.StripeAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Stripe API service that deals with sync requests.
 * Thus, each method must be sync, so a normal method.
 * If you're handling a webhook, see StripeAPIWebhookHandlersService,
 * where methods are async.
 * 
 */
@Service
public class StripeAPIService {
    
    @Autowired
    private StripeAPIProperties APIproperties;

    @Autowired
    private CompaniesService companiesService;
    
    @Autowired
    private FrontendRoutes frontendRoutes;
    
    
    
    /**
     * Create a Stripe customer.
     * We create a Stripe customer at company signup.
     * 
     * @param email
     * @param companyName
     * @return
     */
    public String createCustomer(String email, String companyName) {
        try {
            
            var params = com.stripe.param.CustomerCreateParams
                    .builder()
                    .setEmail(email)
                    .setName(companyName)
                    .build();

            Customer customer = APIproperties.getStripeClient().customers().create(params);
            return customer.getId();

        } catch (StripeException e) {
            throw new StripeAPIException("Failed to create Stripe customer", e);
        }
    }



    /**
     * <h1>Stripe Checkout = create a new subscription</h1>
     * 
     * Create a Stripe Checkout Session for a company.
     * We create a checkout session when user needs to pay the software.
     * We do this after admin verifies their email.
     * 
     *
     * This generates a one-time, hosted Stripe payment page URL.
     * The company is identified by their Stripe customer ID,
     * which was saved during signup.
     *
     * The user will be redirected to this URL to enter their card details.
     * A 14-day free trial is applied before the first charge.
     *
     * @param stripeCustomerId the Stripe customer ID of the company
     * @return the URL of the hosted Stripe checkout page
     */
    public String createCheckoutSession(String stripeCustomerId) {


        // stripe customer ID cannot be null
        if(stripeCustomerId == null) {
            throw new InvalidDataException("While creating a Stripe Checkout Session, "
                    +"the Stripe customer ID cannot be null (null was passed).");
        }
        
        try {
            
            var params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.SUBSCRIPTION)
                    .setCustomer(stripeCustomerId)
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            // the price ID defines what the customer is subscribing to
                            .setPrice(APIproperties.getPriceId())
                            .setQuantity(1L)
                            .build())

                    // 14-day free trial before first charge
                    .setSubscriptionData(
                            SessionCreateParams.SubscriptionData.builder()
                                    .setTrialPeriodDays(14L)
                                    .build()
                    )

                    // where to redirect after successful payment
                    .setSuccessUrl(frontendRoutes.root())
                    // where to redirect if user cancels or closes the checkout page
                    .setCancelUrl(frontendRoutes.root())
                    .build();

            Session session = APIproperties.getStripeClient().checkout().sessions().create(params);

            // return the one-time URL to redirect the user to
            return session.getUrl();

        } catch (StripeException e) {
            throw new StripeAPIException("Failed to create Stripe checkout session", e);
        }
    }


    /**
     * <h1>Stripe Customer Portal = manage existing subscription</h1>
     * 
     * Create Customer Portal session.
     * This is 
     * 
     * @param stripeCustomerId
     * @return
     */
    public String createCustomerPortalSession(String stripeCustomerId) {
        
        // stripe customer ID cannot be null
        if(stripeCustomerId == null) {
            throw new InvalidDataException("While creating a Stripe Customer Portal Session, "
                                            +"the Stripe customer ID cannot be null (null was passed).");
        }
        
        try {
            
            // note: both SessionCreateParams and Session are NOT
            // the same as those in "create checkout session" or "create customer".
            // be aware of using the right class. this is why we use
            // the fully qualified class path instead of the class reference
            
            var params = com.stripe.param.billingportal
                                            .SessionCreateParams.builder()
                                            .setCustomer(stripeCustomerId)
                                            .setReturnUrl(frontendRoutes.dashboard())
                                            .build();
            
            var session = APIproperties.getStripeClient().billingPortal().sessions().create(params);

            return session.getUrl();

        } catch (StripeException e) {
            throw new StripeAPIException("Failed to create billing portal session", e);
        }
    }
    

}
