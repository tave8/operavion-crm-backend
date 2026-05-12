package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.ClientAddressChecklist;

import java.util.UUID;

public class ClientAddressChecklistToSendDTO {
    
    private final UUID id;
    private final UUID checklistId;
    private final UUID clientAddressId;
    
    public ClientAddressChecklistToSendDTO(ClientAddressChecklist clientAddressChecklist) {
        this.id = clientAddressChecklist.getId();
        this.checklistId = clientAddressChecklist.getChecklist().getId();
        this.clientAddressId = clientAddressChecklist.getClientAddress().getId();
    }    
    
    public UUID getId() {
        return id;
    }

    public UUID getChecklistId() {
        return checklistId;
    }

    public UUID getClientAddressId() {
        return clientAddressId;
    }
}
