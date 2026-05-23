package giuseppetavella.demo_login_system.domain.entities.companies;

import giuseppetavella.demo_login_system.exceptions.InvalidDataFormatException;
import jakarta.persistence.*;

import java.time.OffsetDateTime;
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
