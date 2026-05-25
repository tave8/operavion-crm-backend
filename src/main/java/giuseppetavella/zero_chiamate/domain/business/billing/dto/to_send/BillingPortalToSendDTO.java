package giuseppetavella.zero_chiamate.domain.business.billing.dto.to_send;

/**
 * Use this DTO when you're sending the 
 * Stripe Customer Portal.
 * 
 */
public class BillingPortalToSendDTO {
    
    private final String portalUrl;
    
    public BillingPortalToSendDTO(String portalUrl) {
        this.portalUrl = portalUrl;
    }


    public String getPortalUrl() {
        return portalUrl;
    }
    
}
