package giuseppetavella.demo_login_system.entities.checklists;

import giuseppetavella.demo_login_system.entities.Company;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "checklists")
public class Checklist {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false)
    private String name;
    
    protected Checklist() {}
    
    public Checklist(Company company, String name) 
    {
        
        this.company = company;
        this.name = name;
        
    }

    public String getName() {
        return name;
    }

    public Company getCompany() {
        return company;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Checklist{" +
                "company=" + company +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
