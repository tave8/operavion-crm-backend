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

    public ClientAddress(Client client, Address address) {
        this.address = address;
        this.client = client;
    }

    public Address getAddress() {
        return address;
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
