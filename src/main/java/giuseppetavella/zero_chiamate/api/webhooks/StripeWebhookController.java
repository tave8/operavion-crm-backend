package giuseppetavella.zero_chiamate.api.webhooks;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhooks")
public class StripeWebhookController {
    
    @Autowired
    private StripeAPIService stripeAPIService;
    
    @Autowired
    private StripeAPIValidator stripeAPIValidator;
    
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

        // we must make sure that the API versions of what we expect
        // and what Stripe sends, actually match
        stripeAPIValidator.requireStableAPIVersion(event);

        
        // we have the event, now we can process it with custom logic
        // System.out.println(event.getData());
        
        // *********************************
        // HANDLE STRIPE EVENTS
        // *********************************


        switch (event.getType()) {
            case "customer.subscription.updated" -> {
                stripeAPIService.handleSubscriptionUpdated(event);
            }
            case "customer.subscription.deleted" -> {
                // stripeAPIService.handleSubscriptionDeleted(event);
            }
            case "invoice.payment_failed" -> {
                // stripeAPIService.handlePaymentFailed(event);
            }
            default -> {
                // ignore events that we are not interested in
            }
        }
        

        return ResponseEntity.ok().build();
    }
    
}
