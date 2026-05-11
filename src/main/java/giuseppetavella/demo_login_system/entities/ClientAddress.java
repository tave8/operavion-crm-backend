package giuseppetavella.demo_login_system.entities;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(
        name = "client_addresses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"client_id", "address_id"})
        }
)
public class ClientAddress {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
    
    // the name of the client-address serves simply as a label
    @Column(nullable = false)
    private String name;
    
    protected ClientAddress() {}

    public ClientAddress(Client client, Address address, String name) 
    {
        this.address = address;
        this.client = client;
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Client getClient() {
        return client;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ClientAddress{" +
                "address=" + address +
                ", id=" + id +
                ", client=" + client +
                '}';
    }
}
