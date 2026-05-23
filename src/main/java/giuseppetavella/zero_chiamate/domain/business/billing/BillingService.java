package giuseppetavella.zero_chiamate.domain.business.billing;

import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    @Autowired
    private StripeAPIService stripeAPIService;

}
