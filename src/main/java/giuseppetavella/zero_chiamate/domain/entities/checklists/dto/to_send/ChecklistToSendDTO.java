package giuseppetavella.zero_chiamate.domain.entities.checklists.dto.to_send;

import giuseppetavella.zero_chiamate.domain.entities.checklist_entries.dto.to_send.ChecklistEntryToSendDTO;
import giuseppetavella.zero_chiamate.domain.entities.checklists.Checklist;

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
