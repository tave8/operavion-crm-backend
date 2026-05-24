package giuseppetavella.zero_chiamate.integrations.stripe;

import com.stripe.model.Event;
import com.stripe.model.Subscription;
import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.exceptions.NotFoundException;
import giuseppetavella.zero_chiamate.exceptions.integrations.stripe.StripeAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Stripe API service that handles webhooks only.
 * Thus, each method must be async.
 * This service is not for dealing with sync requests.
 * For sync requests, see StripeAPIService.
 * 
 */
@Service
public class StripeAPIWebhookHandlersService {


    @Autowired
    private CompaniesService companiesService;
    

    /**
     * Handle subscription update. 
     *
     * @param event
     */
    @Async
    public void handleSubscriptionUpdated(Event event) {

        
        Subscription subscription = (Subscription) event
                .getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new StripeAPIException("Could not deserialize subscription from event"));

        String stripeCustomerId = subscription.getCustomer();
        String stripeStatus = subscription.getStatus();

        StripeAPISubscriptionStatus newStatus = StripeAPISubscriptionStatus.fromStripe(stripeStatus);

        Company company = companiesService.findByStripeCustomerId(stripeCustomerId)
                .orElseThrow(() -> new NotFoundException("No company found for Stripe customer: " + stripeCustomerId));

        company.setStripeSubscriptionStatus(newStatus);
        companiesService.save(company);
    }

}
