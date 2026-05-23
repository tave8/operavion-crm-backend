package giuseppetavella.demo_login_system.entities.clients;

import giuseppetavella.demo_login_system.entities.Address;
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
    @Column(name = "address_name", nullable = false)
    private String addressName;
    
    protected ClientAddress() {}

    public ClientAddress(Client client, Address address, String addressName) 
    {
        this.address = address;
        this.client = client;
        this.addressName = addressName;
    }

    /**
     * Helper method. Get the client + address name,
     * something like "Hotel Rossi, vicino parco nazionale"
     * @return
     */
    public String getClientAndAddressName() {
        return getClient().getLegalName() + ", " + getAddressName();
    }
    
    public Address getAddress() {
        return address;
    }

    public String getAddressName() {
        return addressName;
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
