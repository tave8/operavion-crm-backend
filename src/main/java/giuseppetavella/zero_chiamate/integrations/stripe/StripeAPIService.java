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
import org.springframework.stereotype.Service;

@Service
public class StripeAPIService {

    // this injects the StripeClient we defined in StripeAPIConfig
    @Autowired
    private StripeClient stripeClient;

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

            Customer customer = stripeClient.customers().create(params);
            return customer.getId();

        } catch (StripeException e) {
            throw new StripeAPIException("Failed to create Stripe customer", e);
        }
    }

    
    /**
     * 
     * 
     * 
     * @param event
     */
    public void handleSubscriptionUpdated(Event event) {

        // var deserializer = event.getDataObjectDeserializer();
        
        // System.out.println(">>> Event type: " + event.getType());
        // System.out.println(">>> Has object: " + deserializer.getObject().isPresent());
        // System.out.println(">>> API version: " + event.getApiVersion());
        //
        // System.out.println(">>> Raw JSON: " + event.getData().getObject().toJson());
        // System.out.println(">>> Object type: " + event.getData().getObject().getClass().getName());
        //
        // System.out.println(">>> Raw object: " + event.getData().toJson());
        
        
        
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
