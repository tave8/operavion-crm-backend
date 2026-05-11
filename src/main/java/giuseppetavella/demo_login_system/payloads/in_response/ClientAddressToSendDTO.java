package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.ClientAddress;

import java.util.UUID;

public class ClientAddressToSendDTO {
    private final UUID clientId;
    private final UUID addressId;
    private final String addressName;

    public ClientAddressToSendDTO(ClientAddress clientAddress) {
        this.addressId = clientAddress.getAddress().getId();
        this.clientId = clientAddress.getClient().getId();
        this.addressName = clientAddress.getAddressName();
    }

    public UUID getAddressId() {
        return addressId;
    }

    public String getAddressName() {
        return addressName;
    }

    public UUID getClientId() {
        return clientId;
    }
}
