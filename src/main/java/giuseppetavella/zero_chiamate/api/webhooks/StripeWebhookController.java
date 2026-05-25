package giuseppetavella.zero_chiamate.api.webhooks;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import giuseppetavella.zero_chiamate.domain.business.auth.AuthEmailService;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIProperties;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIService;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIValidator;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPIWebhookHandlersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private StripeAPIWebhookHandlersService webhookHandlersService;
    
    @Autowired
    private StripeAPIValidator stripeAPIValidator;
    
    @Autowired
    private StripeAPIProperties APIproperties;

    // logger
    private static final Logger LOGGER = LoggerFactory.getLogger(StripeWebhookController.class);




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
            event = Webhook.constructEvent(payload, sigHeader, APIproperties.getWebhookSecret());
            
        } catch (SignatureVerificationException e) {
        
            // verification failed: possible causes:
            // event is not coming from Stripe, etc.
            return ResponseEntity.status(400).body("Invalid signature");
        
        }

        
        // we have the event, now we can process it with custom logic
        
        // *********************************
        // HANDLE STRIPE EVENTS
        // *********************************
        
        // we ignore events that we are not interested in,
        // but we run checks on the evens we are intested in
        
        var wasInterestedEvent = true;

        switch (event.getType()) {
            // fix: when the user first signs up (in the Stripe-hosted checkout page), 
            // the subscription is created, so we need to handle it
            case "customer.subscription.created", "customer.subscription.updated" -> {
                // we must make sure that the API versions of what we expect
                // and what Stripe sends, actually match
                stripeAPIValidator.requireStableAPIVersion(event);

                webhookHandlersService.handleSubscriptionUpdated(event);
            }

            case "customer.subscription.deleted" -> {
                // we must make sure that the API versions of what we expect
                // and what Stripe sends, actually match
                stripeAPIValidator.requireStableAPIVersion(event);

                webhookHandlersService.handleSubscriptionDeleted(event);
            }
            case "invoice.payment_failed" -> {
                // we must make sure that the API versions of what we expect
                // and what Stripe sends, actually match
                stripeAPIValidator.requireStableAPIVersion(event);

                
                // stripeAPIService.handlePaymentFailed(event);
            }
            default -> {
                
                wasInterestedEvent = false;
                // ignore events that we are not interested in
            }
        }
        
        // we only log relevant event types 
        if(wasInterestedEvent) {
            
            LOGGER.info("Stripe API: webhook with event type '{}' fired.", event.getType());
            
        }
        

        return ResponseEntity.ok().build();
    }
    
}
