package giuseppetavella.zero_chiamate.integrations.stripe;

import com.stripe.model.Event;
import giuseppetavella.zero_chiamate.exceptions.integrations.stripe.StripeAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Class to deal with Stripe API versioning
 * and errors that might occur silently. 
 */
@Component
public class StripeAPIValidator {

    // injected dependency
    private final String expectedStripeAPIVersion;
    
    public StripeAPIValidator(@Qualifier("expectedStripeAPIVersion") String expectedStripeAPIVersion) 
    {
        this.expectedStripeAPIVersion = expectedStripeAPIVersion;    
    }
    
    
    /**
     * Require that the API version of an event sent by Stripe API 
     * matches exactly the API version that we defined.
     *
     * @param event the event sent by Stripe API
     */
    public void requireStableAPIVersion(Event event) throws StripeAPIException
    {

        if(event == null) {
            throw new StripeAPIException("While checking whether an Event of Stripe API "
                                        +"has a fixed version, the event is null.");
        }
        
        if (!isSameAPIVersion(event))
        {
            throw new StripeAPIException(
                    "Stripe API version mismatch detected. " +
                            "Expected: '" + expectedStripeAPIVersion + "'. " +
                            "Received: '" + event.getApiVersion() + "'. " +
                            "Event type: '" + event.getType() + "'. " +
                            "Event ID: '" + event.getId() + "'. " +
                            "This likely means the Stripe account's default API version was changed. " +
                            "Update the expected Stripe API version in Stripe configuration, "
                            +"or roll back the version in the Stripe Dashboard."
            );
        }
    }


    /**
     * Is the API version of the event sent by Stripe,
     * the same that we defined? 
     * 
     * @return
     */
    public boolean isSameAPIVersion(Event event) 
    {
    
        if(event == null) {
            throw new StripeAPIException("While checking whether an Event of Stripe API "
                                        +"has a fixed version, the event is null.");
        }
        
        return expectedStripeAPIVersion.equals(event.getApiVersion());
        
    }

    
}
