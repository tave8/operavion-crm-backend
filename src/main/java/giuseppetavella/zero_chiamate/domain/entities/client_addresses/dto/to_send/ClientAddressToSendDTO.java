package giuseppetavella.zero_chiamate.domain.entities.client_addresses.dto.to_send;

import com.fasterxml.jackson.annotation.JsonIgnore;
import giuseppetavella.zero_chiamate.domain.entities.client_addresses.ClientAddress;
import giuseppetavella.zero_chiamate.domain.entities.contract_expectations.dto.to_send.ContractExpectationToSendDTO;

import java.util.UUID;

public class ClientAddressToSendDTO {
    private final UUID id;
    private final UUID clientId;
    private final UUID addressId;
    private final String addressName;
    private final String clientName;
    private final String addressDisplayName;
    private final double addressLat;
    private final double addressLon;
    private final ContractExpectationToSendDTO contractExpectation;
    
    // this field will be ignored both on read and on write,
    // but i need it internally in memory
    @JsonIgnore
    private final ClientAddress clientAddress;

    public ClientAddressToSendDTO(ClientAddress clientAddress, 
                                  ContractExpectationToSendDTO contractExpectation) 
    {
        this.id = clientAddress.getId();
        this.addressId = clientAddress.getAddress().getId();
        this.clientId = clientAddress.getClient().getId();
        this.addressName = clientAddress.getAddressName();
        this.clientName = clientAddress.getClient().getLegalName();
        this.addressDisplayName = clientAddress.getAddress().getDisplayName();
        this.addressLat = clientAddress.getAddress().getLat();
        this.addressLon = clientAddress.getAddress().getLon();
        this.contractExpectation = contractExpectation;
        
        // note: this attribute will be ignored both on read and write,
        // it is used only for internal purposes
        this.clientAddress = clientAddress;
    }

    public UUID getAddressId() {
        return addressId;
    }

    public ContractExpectationToSendDTO getContractExpectation() {
        return contractExpectation;
    }

    public String getAddressDisplayName() {
        return addressDisplayName;
    }

    public double getAddressLat() {
        return addressLat;
    }

    public String getClientName() {
        return clientName;
    }

    public UUID getId() {
        return id;
    }

    public double getAddressLon() {
        return addressLon;
    }

    public String getAddressName() {
        return addressName;
    }

    public UUID getClientId() {
        return clientId;
    }

    public ClientAddress getClientAddress() {
        return clientAddress;
    }
}
