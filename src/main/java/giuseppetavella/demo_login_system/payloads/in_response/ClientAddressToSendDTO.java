package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;

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

    public ClientAddressToSendDTO(ClientAddress clientAddress) {
        this.id = clientAddress.getId();
        this.addressId = clientAddress.getAddress().getId();
        this.clientId = clientAddress.getClient().getId();
        this.addressName = clientAddress.getAddressName();
        this.clientName = clientAddress.getClient().getLegalName();
        this.addressDisplayName = clientAddress.getAddress().getDisplayName();
        this.addressLat = clientAddress.getAddress().getLat();
        this.addressLon = clientAddress.getAddress().getLon();
    }

    public UUID getAddressId() {
        return addressId;
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
}
