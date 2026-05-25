package giuseppetavella.zero_chiamate.domain.entities.companies.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.companies.Company;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public class CompanyToSendDTO {
    
    private final UUID id;
    private final String legalName;
    private final String email;
    private final String stripeCustomerId;
    private final StripeAPISubscriptionStatus stripeSubscriptionStatus;
    private final OffsetDateTime createdAt;
    
    public CompanyToSendDTO(Company company) {
        this.id = company.getId();
        this.legalName = company.getLegalName();
        this.email = company.getEmail();
        this.stripeCustomerId = company.getStripeCustomerId();
        this.stripeSubscriptionStatus = company.getStripeSubscriptionStatus();
        this.createdAt = company.getCreatedAt();
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    public String getLegalName() {
        return legalName;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }

    public StripeAPISubscriptionStatus getStripeSubscriptionStatus() {
        return stripeSubscriptionStatus;
    }
}
