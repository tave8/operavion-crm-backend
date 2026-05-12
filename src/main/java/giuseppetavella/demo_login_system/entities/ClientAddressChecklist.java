package giuseppetavella.demo_login_system.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "client_address_checklists",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = { "client_address_id", "checklist_id"})
        }
)
public class ClientAddressChecklist {
    
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "client_address_id", nullable = false)
    private ClientAddress clientAddress;
    
    @ManyToOne
    @JoinColumn(name = "checklist_id", nullable = false)
    private Checklist checklist;
    
    protected ClientAddressChecklist() {}

    public ClientAddressChecklist(ClientAddress clientAddress, Checklist checklist) 
    {
        
        this.clientAddress = clientAddress;
        this.checklist = checklist;
        
    }

    public Checklist getChecklist() {
        return checklist;
    }

    public UUID getId() {
        return id;
    }

    public ClientAddress getClientAddress() {
        return clientAddress;
    }

    @Override
    public String toString() {
        return "ClientAddressChecklist{" +
                "checklist=" + checklist +
                ", id=" + id +
                ", clientAddress=" + clientAddress +
                '}';
    }
}
