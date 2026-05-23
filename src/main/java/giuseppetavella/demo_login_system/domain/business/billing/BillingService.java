package giuseppetavella.demo_login_system.domain.business.billing;

import giuseppetavella.demo_login_system.integrations.stripe.StripeAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BillingService {

    @Autowired
    private StripeAPIService stripeAPIService;

}
