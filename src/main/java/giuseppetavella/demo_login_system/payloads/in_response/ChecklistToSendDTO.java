package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.Checklist;

import java.util.UUID;

public class ChecklistToSendDTO {
    
    private final UUID id;
    private final String name;
    
    public ChecklistToSendDTO(Checklist checklist) {
        this.id = checklist.getId();
        this.name = checklist.getName();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
