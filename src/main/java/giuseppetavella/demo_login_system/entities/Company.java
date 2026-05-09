package giuseppetavella.demo_login_system.entities;

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
