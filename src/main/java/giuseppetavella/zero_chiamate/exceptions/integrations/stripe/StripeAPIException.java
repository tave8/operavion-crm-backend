package giuseppetavella.zero_chiamate.exceptions.integrations.stripe;

public class StripeAPIException extends RuntimeException {
    public StripeAPIException(String message, Exception ex) {
        super("Error while working with Stripe API. DETAILS: "+message+". DETAILS: " + ex.getMessage());
    }
    
    public StripeAPIException(String message) {
        super("Error while working with Stripe API. DETAILS: "+message);
    }
}
