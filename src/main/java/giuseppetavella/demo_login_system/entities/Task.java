package giuseppetavella.demo_login_system.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Company company;
    
    @Column(nullable = false)
    private String name;
    
    protected Task() {}
    
    public Task(Company company, String name) 
    {
        
        this.company = company;
        this.name = name;
        
    }

    public Company getCompany() {
        return company;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Task{" +
                "company=" + company +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
