package giuseppetavella.zero_chiamate.exceptions;

import java.util.UUID;

public class ClientAddressChecklistException extends RuntimeException {
    public ClientAddressChecklistException(String message) {
        super("Error while working with an association between a checklist and a client address. DETAILS: " + message);
    }
    
     public ClientAddressChecklistException(UUID clientAddressId,UUID checklistId, String message) {
        super("Error while working with an association between a checklist and "
                +"a client address. Checklist ID: " + checklistId + ", client address ID: " + clientAddressId + ". DETAILS: " + message);
        
     }   
}
