package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.Client;

import java.util.UUID;

public class ClientToSendDTO {
    
    private final UUID clientId;
    private final String legalName;
    private final String email;
    private final String phone;
    private final String legalAddress;
    private final double legalAddressLat;
    private final double legalAddressLon;

    public ClientToSendDTO(Client client) {
        this.clientId = client.getId();
        this.legalName = client.getLegalName();
        this.email = client.getEmail();
        this.legalAddress = client.getLegalAddress().getDisplayName();
        this.legalAddressLat = client.getLegalAddress().getLat();
        this.legalAddressLon = client.getLegalAddress().getLon();
        this.phone = client.getPhone();
    }

    public UUID getClientId() {
        return clientId;
    }

    public String getEmail() {
        return email;
    }

    public String getLegalAddress() {
        return legalAddress;
    }

    public double getLegalAddressLat() {
        return legalAddressLat;
    }

    public String getLegalName() {
        return legalName;
    }

    public double getLegalAddressLon() {
        return legalAddressLon;
    }

    public String getPhone() {
        return phone;
    }
}
