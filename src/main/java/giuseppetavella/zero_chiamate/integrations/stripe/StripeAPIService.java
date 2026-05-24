package giuseppetavella.zero_chiamate.integrations.stripe;

import com.stripe.StripeClient;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import giuseppetavella.zero_chiamate.exceptions.integrations.stripe.StripeAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StripeAPIService {

    // this injects the StripeClient we defined in StripeAPIConfig
    @Autowired
    private StripeClient stripeClient;
    

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

}
