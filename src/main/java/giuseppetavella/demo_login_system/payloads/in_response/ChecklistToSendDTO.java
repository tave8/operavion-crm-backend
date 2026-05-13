package giuseppetavella.demo_login_system.payloads.in_response;

import giuseppetavella.demo_login_system.entities.Checklist;

import java.util.List;
import java.util.UUID;

public class ChecklistToSendDTO {
    
    private final UUID id;
    private final String name;
    private final List<ChecklistEntryToSendDTO> entries;
    
    public ChecklistToSendDTO(Checklist checklist, 
                              List<ChecklistEntryToSendDTO> entries) 
    {
        this.id = checklist.getId();
        this.name = checklist.getName();
        this.entries = entries;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<ChecklistEntryToSendDTO> getEntries() {
        return entries;
    }
}
