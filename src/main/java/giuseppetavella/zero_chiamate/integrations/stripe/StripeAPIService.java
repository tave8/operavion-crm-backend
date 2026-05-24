package giuseppetavella.zero_chiamate.integrations.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.Event;
import com.stripe.model.Subscription;
import com.stripe.param.CustomerCreateParams;
import giuseppetavella.zero_chiamate.domain.entities.companies.CompaniesService;
import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
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
    
    
    
    /**
     * Create a Stripe customer.
     * 
     * @param email
     * @param companyName
     * @return
     */
    public String createCustomer(String email, String companyName) {
        try {
            CustomerCreateParams params = CustomerCreateParams.builder()
                    .setEmail(email)
                    .setName(companyName)
                    .build();

            Customer customer = APIproperties.getStripeClient().customers().create(params);
            return customer.getId();

        } catch (StripeException e) {
            throw new StripeAPIException("Failed to create Stripe customer", e);
        }
    }


}
