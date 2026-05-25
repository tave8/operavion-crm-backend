package giuseppetavella.zero_chiamate.domain.business.billing.dto.to_send;

/**
 * A checkout is the term used by Stripe
 * to refer to creating a new subscription.
 */
public class BillingCheckoutToSendDTO {
    
    private final String checkoutUrl;
    
    public BillingCheckoutToSendDTO(String checkoutUrl) {
        this.checkoutUrl = checkoutUrl;
    }

    public String getCheckoutUrl() {
        return checkoutUrl;
    }
}
