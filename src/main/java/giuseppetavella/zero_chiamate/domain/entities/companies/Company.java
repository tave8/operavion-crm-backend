package giuseppetavella.zero_chiamate.domain.entities.companies;

import giuseppetavella.zero_chiamate.exceptions.BillingException;
import giuseppetavella.zero_chiamate.exceptions.InvalidDataFormatException;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
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

    
    protected Company() {}
    
    public Company(String legalName, String email) {
        this.email = email;
        this.legalName = legalName;
        this.createdAt = OffsetDateTime.now();
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
