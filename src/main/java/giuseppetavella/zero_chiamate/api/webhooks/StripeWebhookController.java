package giuseppetavella.zero_chiamate.api.webhooks;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import giuseppetavella.zero_chiamate.domain.business.billing.BillingService;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class StripeWebhookController {
    
    @Autowired
    private StripeAPIService stripeAPIService;
    
    // dependency-injected
    private final String webhookSecret;
    
    public StripeWebhookController(
            @Qualifier("stripeAPIWebhookSecret") String stripeAPIWebhookSecret) 
    {
        this.webhookSecret = stripeAPIWebhookSecret;
    }


    /**
     * <h1>Webhook that receives events from Stripe API</h1>
     *
     * 
     * @param payload
     * @param sigHeader
     * @return
     */
    @PostMapping("/stripe")
    public ResponseEntity<?> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) 
    {
        
        // Stripe event
        Event event;

        // *********************************
        // VERIFY SIGNATURE
        // *********************************
        
        try {
            // - verify that the request is coming from Stripe
            // - deserialize payload string into actual Stripe Event instance
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            
        } catch (SignatureVerificationException e) {
        
            // verification failed: possible causes:
            // event is not coming from Stripe, etc.
            return ResponseEntity.status(400).body("Invalid signature");
        
        }

        // stripeAPIService.createCustomer("giuseppetavella8@gmail.com", "Giuseppe Tavella");

        // System.out.println("customer created");

        
        // *********************************
        // HANDLE STRIPE EVENTS
        // *********************************

        
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
