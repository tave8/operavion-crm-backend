package giuseppetavella.demo_login_system.dto;

import giuseppetavella.demo_login_system.entities.clients.ClientAddress;

/**
 * DTO representing a client address - discrepancy entry.
 */
public class ClientAddressDiscrepancyDTO {
    
    private final String clientName;
    private final String addressName;
    private final String discrepancy;
    
    public ClientAddressDiscrepancyDTO(ClientAddress clientAddress,
                                       String discrepancy) 
    {
    
        this.clientName = clientAddress.getClient().getLegalName();
        this.addressName = clientAddress.getAddressName();
        this.discrepancy = discrepancy;
        
    }

    public String getAddressName() {
        return addressName;
    }

    public String getDiscrepancy() {
        return discrepancy;
    }

    public String getClientName() {
        return clientName;
    }
}
