package giuseppetavella.zero_chiamate.domain.entities.companies;

import giuseppetavella.zero_chiamate.exceptions.BillingException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataFormatException;
import giuseppetavella.zero_chiamate.exceptions.InvalidStateTransitionException;
import giuseppetavella.zero_chiamate.helpers.DataValidationHelper;
import giuseppetavella.zero_chiamate.integrations.stripe.StripeAPISubscriptionStatus;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "companies")
public class Company {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(name = "legal_name", nullable = false)
    private String legalName;
    
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    /**
     * We use this ID to identify the company
     * in Stripe API.
     */
    @Column(name = "stripe_customer_id", unique = true)
    private String stripeCustomerId;

    /**
     * The subscription status as a Stripe customer. 
     */
    @Column(name = "stripe_subscription_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StripeAPISubscriptionStatus stripeSubscriptionStatus;

    
    protected Company() {}
    
    public Company(String legalName, String email) {
        this.email = email;
        this.legalName = legalName;
        this.createdAt = OffsetDateTime.now();
        // when company is first created, Stripe subscription status
        // is incomplete
        this.stripeSubscriptionStatus = StripeAPISubscriptionStatus.INCOMPLETE;
    }

    /**
     * Are the given companies the same?
     */
    public static boolean isSameCompany(Company company1, Company company2) throws InvalidDataFormatException
    {

        if(company1 == null || company2 == null) {
            throw new InvalidDataFormatException(
                    "While validating whether two companies are the same, "
                            +"either one or both of them were null. Are you sure both companies exist "
                            +"or have been passed correctly?"
            );
        }

        return company1.getId().equals(company2.getId());
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getLegalName() {
        return legalName;
    }

    public StripeAPISubscriptionStatus getStripeSubscriptionStatus() {
        return stripeSubscriptionStatus;
    }

    /**
     * We set the Stripe customer ID
     * after the company is saved to DB.
     * 
     * @param stripeCustomerId
     */
    public void setStripeCustomerId(String stripeCustomerId) throws BillingException 
    {
        // if Stripe company ID exists, throw
        if(getStripeCustomerId() != null) {
            throw new BillingException("While setting the Stripe customer ID "
                                        +"for company with ID " + getId() + ", this company "
                                        +"already has a non-null Stripe customer ID.");
        }
        this.stripeCustomerId = stripeCustomerId;
    }

    public String getStripeCustomerId() {
        return stripeCustomerId;
    }


    /**
     * <pre>
     * no state      ->  INCOMPLETE
     * INCOMPLETE    ->  TRIALING | ACTIVE
     * TRIALING      ->  ACTIVE | PAST_DUE
     * ACTIVE        ->  PAST_DUE | CANCELED
     * PAST_DUE      ->  ACTIVE | CANCELED
     * CANCELED      ->  
     * </pre>
     */
    public void setStripeSubscriptionStatus(StripeAPISubscriptionStatus stripeSubscriptionStatus) throws InvalidStateTransitionException
    {
        StripeAPISubscriptionStatus currentStatus = this.getStripeSubscriptionStatus();

        boolean noStateYet = currentStatus == null;

        List<StripeAPISubscriptionStatus> firstStates = List.of(StripeAPISubscriptionStatus.INCOMPLETE);

        Map<StripeAPISubscriptionStatus, List<StripeAPISubscriptionStatus>> stateMap = Map.of(
                StripeAPISubscriptionStatus.INCOMPLETE, List.of(StripeAPISubscriptionStatus.TRIALING, StripeAPISubscriptionStatus.ACTIVE),
                StripeAPISubscriptionStatus.TRIALING,   List.of(StripeAPISubscriptionStatus.ACTIVE, StripeAPISubscriptionStatus.PAST_DUE),
                StripeAPISubscriptionStatus.ACTIVE,     List.of(StripeAPISubscriptionStatus.PAST_DUE, StripeAPISubscriptionStatus.CANCELED),
                StripeAPISubscriptionStatus.PAST_DUE,   List.of(StripeAPISubscriptionStatus.ACTIVE, StripeAPISubscriptionStatus.CANCELED),
                StripeAPISubscriptionStatus.CANCELED,   List.of()
        );

        DataValidationHelper.requireValidStateTransition(
                StripeAPISubscriptionStatus.class,
                currentStatus,
                stripeSubscriptionStatus,
                firstStates,
                stateMap,
                noStateYet,
                "stripe subscription"
        );

        this.stripeSubscriptionStatus = stripeSubscriptionStatus;
    }
    
    

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Company{" +
                "createdAt=" + createdAt +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", legalName='" + legalName + '\'' +
                '}';
    }
}
