package giuseppetavella.zero_chiamate.api.webhooks;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import giuseppetavella.zero_chiamate.domain.business.billing.BillingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class StripeWebhookController {
    
    @Autowired
    private BillingService billingService;
    
    // dependency-injected
    private final String webhookSecret;
    
    public StripeWebhookController(
            @Qualifier("stripeAPIWebhookSecret") String stripeAPIWebhookSecret) 
    {
        this.webhookSecret = stripeAPIWebhookSecret;
    }
    

    @PostMapping("/stripe")
    public ResponseEntity<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) 
    {

        Event event;

        try {
            
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            
        } catch (SignatureVerificationException e) {
        
            return ResponseEntity.status(400).body("Invalid signature");
        
        }

        System.out.println(event);
        
        switch (event.getType()) {
            case "customer.subscription.updated" -> {
                // billingService.handleSubscriptionUpdated(event);
            }
            case "customer.subscription.deleted" -> {
                // billingService.handleSubscriptionDeleted(event);
            }
            case "invoice.payment_failed" -> {
                // billingService.handlePaymentFailed(event);
            }
            default -> {
                // ignore unhandled events
            }
        }
        

        return ResponseEntity.ok().build();
    }
    
}
