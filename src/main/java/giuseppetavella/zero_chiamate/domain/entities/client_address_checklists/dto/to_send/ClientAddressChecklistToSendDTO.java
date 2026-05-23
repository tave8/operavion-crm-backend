package giuseppetavella.zero_chiamate.domain.entities.client_address_checklists.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.client_address_checklists.ClientAddressChecklist;

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
